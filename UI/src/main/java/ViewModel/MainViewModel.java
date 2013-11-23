package ViewModel;

import com.google.inject.Inject;
import javafx.application.HostServices;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import model.ProductWithStyle;
import utility.ProductTaskFactory;
import utility.ViewManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main View Model of the application
 * Contains all the properties that are used by the controllers.
 * Contains the actual execution code/logic
 */
public class MainViewModel {
    //list of subscribed items
    private ObservableList<ProductWithStyleViewModel> subscribedItems;
    //email
    private StringProperty email;
    //query term
    private StringProperty queryProduct;
    //state of add button
    private BooleanProperty addButtonDisabled;
    //state of remove button
    private BooleanProperty removeGetInfoDisabled;
    //state of subscribe button
    private BooleanProperty subsribeButtonDisabled;
    //error message
    private SimpleStringProperty error;
    //is email valid
    private SimpleBooleanProperty validEmail;
    //search results
    private ProductWithStyle[] searchResults;
    //added ids
    private HashSet<String> addedIds;
    @Inject
    private ProductTaskFactory productTaskFactory;
    @Inject
    private Handler logger;
    @Inject
    private HostServices host;

    /**
     * Constructor
     */
    public MainViewModel() {
        subscribedItems = FXCollections.observableArrayList();
        email = new SimpleStringProperty();
        queryProduct = new SimpleStringProperty();
        addButtonDisabled = new SimpleBooleanProperty();
        addButtonDisabled.bind(queryProduct.isEqualTo(""));
        //if items in list is ==0,disable button
        removeGetInfoDisabled = new SimpleBooleanProperty(subscribedItemsProperty().isEmpty());
        subscribedItems.addListener(new ListChangeListener<ProductWithStyleViewModel>() {
            @Override
            public void onChanged(Change<? extends ProductWithStyleViewModel> change) {
                removeGetInfoDisabled.setValue(subscribedItemsProperty().isEmpty());
            }
        }
        );
        addedIds = new HashSet<String>();
        error = new SimpleStringProperty();
        //valid email listener
        final Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        validEmail = new SimpleBooleanProperty(true);
        emailProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                Matcher m = p.matcher(s2);
                if (m.matches()) {
                    validEmail.set(false);
                } else {
                    validEmail.set(true);
                }
            }
        });
    }

    /**
     * Fetech item from server for query term
     */
    public void addProduct() {
        final String queryProductId = queryProduct.get();
        final Task<ProductWithStyle[]> product = productTaskFactory.getProduct(queryProductId);

        //if the query product has multiple styles, show, else add style
        product.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                ProductWithStyle[] data = product.getValue();
                if (data != null) {
                    if (data.length == 1) {
                        if (addedIds.contains(data[0].styleId)) {
                            error.setValue("Style:" + data[0].styleId + " already in list");
                        } else {
                            //only one option
                            subscribedItems.add(new ProductWithStyleViewModel(data[0]));
                        }
                    } else {
                        searchResults = data;
                        error.set("Multiple styles for product " + queryProductId);
                        //show multiple options window
                        showMultipleStyleWindow();
                    }
                } else {
                    error.setValue("No Product found with id: " + queryProductId);
                }
            }
        });
        product.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                error.setValue("Failed to talk to server");
            }
        });
        Thread th = new Thread(product);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Get Serach Results for last searched term
     * @return search results
     */
    public ProductWithStyle[] getSearchResults() {
        return searchResults;
    }

    /**
     * Show the search results
     */
    private void showMultipleStyleWindow() {
        ViewManager.displayMultipleStyleWindow(queryProduct.get());
    }

    /**
     * Add selected items from search  results to subscription list
     * @param item
     */
    public void addMultiple(Collection<ProductWithStyleViewModel> item) {
        int duplicatesFound = 0;
        //for all selected items, check if item is already in list
        for (ProductWithStyleViewModel p : item) {
            if (!addedIds.contains(p.styleIdProperty().get())) {
                subscribedItems.add(p);
                addedIds.add(p.styleIdProperty().get());
            } else {
                duplicatesFound++;
            }
        }
        if (duplicatesFound != 0) {
            error.set("Did not add " + duplicatesFound + " items");
        } else {
            error.set("");
        }
    }

    /**
     * Update the subscription list
     */
    public void subscribe() {
        Task submit = productTaskFactory.synchronize(getEmail(), subscribedItems.toArray(new ProductWithStyleViewModel[subscribedItems.size()]));
        error.set("Syncing");
        submit.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                error.set("Subscribed to " + subscribedItems.size() + " items");
            }
        });
        submit.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                error.set("Failed to sync");
            }
        });
        Thread th = new Thread(submit);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Retrieve existing subscriptions for user
     */
    public void login() {
        ViewManager.hideChildView();
        final Task<ProductWithStyle[]> task = productTaskFactory.registerAndGetRegisteredProducts(email.get());
        error.set("Fetching Info");
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                ProductWithStyle[] data = task.getValue();
                if (data != null) {
                    for (ProductWithStyle pws : data) {
                        ProductWithStyleViewModel productWithStyleViewModel = new ProductWithStyleViewModel(pws);
                        subscribedItems.add(productWithStyleViewModel);
                        addedIds.add(productWithStyleViewModel.styleIdProperty().get());
                    }
                }
                error.set("Subscribed to " + subscribedItems.size() + " items");
                logger.publish(new LogRecord(Level.INFO, "Fetched items:" + subscribedItems.size()));
            }
        });
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public SimpleBooleanProperty validEmailProperty() {
        return validEmail;
    }

    public final String getError() {
        return error.get();
    }

    public void setError(String msg) {
        error.setValue(msg);
    }

    public SimpleStringProperty errorProperty() {
        return error;
    }

    public final String getEmail() {
        return email.get();
    }

    public void setEmail(String newEmail) {
        email.setValue(newEmail);
    }

    public final StringProperty emailProperty() {
        return email;
    }

    public void setQueryProduct(String p) {
        queryProduct.setValue(p);
    }

    public StringProperty queryProductProperty() {
        return queryProduct;
    }

    public ObservableList<ProductWithStyleViewModel> subscribedItemsProperty() {
        return subscribedItems;
    }

    public void removeProduct(int i) {
        subscribedItems.remove(i);
    }

    public void getInfoOnProduct(int i) {
        String url = subscribedItems.get(i).getWebURL();
        host.showDocument(url);
    }

    public BooleanProperty removeGetInfoDisabledProperty() {
        return removeGetInfoDisabled;
    }

    public boolean geRemoveGetInfoDisabled() {
        return removeGetInfoDisabled.getValue();
    }

    public boolean getAddButtonDisabled() {
        return addButtonDisabled.get();
    }

    public BooleanProperty addButtonDisabledProperty() {
        return addButtonDisabled;
    }
}
