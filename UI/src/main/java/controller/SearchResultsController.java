package controller;

import ViewModel.MainViewModel;
import ViewModel.ProductWithStyleViewModel;
import com.google.inject.Inject;
import customUIComponents.ProductCell;
import javafx.application.HostServices;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.ProductWithStyle;
import utility.ViewManager;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * Controller responsible for showing search results for a product with multiple styles
 */
public class SearchResultsController implements Initializable {
    @FXML
    private Button addButton;
    @FXML
    private Label SelectedCountLabel;
    @FXML
    private ListView<ProductWithStyleViewModel> resultsListView;



    private MainViewModel pvm;

    private HostServices host;

    //keep track of weather to enable the add button
    private SimpleBooleanProperty addSelectedDisabled;
   //styles for product term
    private ObservableList<ProductWithStyleViewModel> products;
    private SimpleStringProperty selectedText;
    //selected items count
    private SimpleIntegerProperty selectedCount;
    //selected items
    private HashSet<ProductWithStyleViewModel> selectedItems;

    @Inject
    public SearchResultsController(MainViewModel pvm,  HostServices host ) {

        this.pvm=pvm;
        this.host=host;
        products = FXCollections.observableArrayList();
        addSelectedDisabled = new SimpleBooleanProperty(true);
        selectedText = new SimpleStringProperty();
        selectedItems = new HashSet<>();
        selectedText.set("Selected " + selectedItems.size() + " items");
        selectedCount= new SimpleIntegerProperty(0);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {




        //To change body of implemented methods use File | Settings | File Templates.

        for (ProductWithStyle p : pvm.getSearchResults()) {
            final ProductWithStyleViewModel e = new ProductWithStyleViewModel(p);
            e.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean newValue) {
                    if (newValue && !selectedItems.contains(e)) {
                        selectedCount.set(selectedCount.get()+1);

                        selectedItems.add(e);
                    } else if (!newValue) {
                        selectedCount.set(selectedCount.get()-1);
                        selectedItems.remove(e);
                    }
                    selectedText.set("Selected " + selectedItems.size() + " items");
                }
            });
            products.add(e);
        }



        addSelectedDisabled.bind(selectedCount.isEqualTo(0));
        SelectedCountLabel.textProperty().bind(selectedText);
        resultsListView.setCellFactory(new Callback<ListView<ProductWithStyleViewModel>, ListCell<ProductWithStyleViewModel>>() {
            @Override
            public ListCell<ProductWithStyleViewModel> call(ListView<ProductWithStyleViewModel> list) {
                ProductCell p = new ProductCell();
                return p;
            }
        });
        resultsListView.setItems(products);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pvm.addMultiple(selectedItems);
                ViewManager.hideChildView();
            }
        });

        //open browser if user clicked on list view
        resultsListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if (number != number2) {
                    ProductWithStyleViewModel productWithStyleViewModel = resultsListView.getItems().get(number2.intValue());
                    host.showDocument(productWithStyleViewModel.getWebURL());
                }
            }
        });

        addButton.disableProperty().bind(addSelectedDisabled);
    }
}
