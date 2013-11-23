package controller;

import ViewModel.MainViewModel;
import ViewModel.ProductWithStyleViewModel;
import com.google.inject.Inject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller fro the initial/Main Screen
 */
public class MainViewController implements Initializable {


    @FXML
    TextField ProductIdPrompt;
    @FXML
    public TableView<ProductWithStyleViewModel> Products;
    @FXML
    TableColumn<ProductWithStyleViewModel, String> productIdCol;
    @FXML
    TableColumn<ProductWithStyleViewModel, String> productNameCol;
    @FXML
    TableColumn<ProductWithStyleViewModel, String> productPriceCol;
    @FXML
    TableColumn<ProductWithStyleViewModel, String> imageCol;
    @FXML
    Button SubscribeBtn;
    @FXML
    Button RemoveBtn;
    @FXML
    Button InfoBtn;
    @FXML
    Button AddProductBtn;
    @FXML
    Label errorLabel;

    @FXML
    private Label emailLabel;

    //main view model
    private MainViewModel pvm;

    /**
     *  Main View Controller
     * @param pvm
     */
    @Inject
    public MainViewController(MainViewModel pvm) {

        this.pvm = pvm;
    }

    /**
     * Initialize view
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //update label based on email address
        pvm.emailProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                emailLabel.setText("Subscription List for " + s2);
            }
        });

        //set error label value to view model error property
        errorLabel.textProperty().bind(pvm.errorProperty());
        //bind product query to view model
        ProductIdPrompt.textProperty().bindBidirectional(pvm.queryProductProperty());
        //set subscribed products to view model's subscribed
        Products.setItems(pvm.subscribedItemsProperty());

        //bind table columns
        productIdCol.setCellValueFactory(new PropertyValueFactory<ProductWithStyleViewModel, String>("styleId"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<ProductWithStyleViewModel, String>("productName"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<ProductWithStyleViewModel, String>("originalPrice"));
        imageCol.setCellValueFactory(new PropertyValueFactory<ProductWithStyleViewModel, String>("imageUrl"));
        Products.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //set image column to custom factory that displays image
        imageCol.setCellFactory(new Callback<TableColumn<ProductWithStyleViewModel, String>, TableCell<ProductWithStyleViewModel, String>>() {
            @Override
            public TableCell<ProductWithStyleViewModel, String> call(TableColumn<ProductWithStyleViewModel, String> productWithStyleViewModelStringTableColumn) {
                return new TableCell<ProductWithStyleViewModel, String>() {
                    @Override
                    public void updateItem(String url, boolean empty) {
                        if (url != null) {
                            VBox vb = new VBox();
                            vb.setAlignment(Pos.CENTER);
                            Image i = new Image(url);
                            ImageView imgVw = new ImageView(i);
                            imgVw.setFitHeight(50);
                            imgVw.setFitWidth(50);
                            setGraphic(imgVw);
                        }
                    }
                };
            }
        });


        //bind state of remove button
        RemoveBtn.disableProperty().bind(pvm.removeGetInfoDisabledProperty());
        //bind state of subscribe button
        SubscribeBtn.disableProperty().bind(pvm.removeGetInfoDisabledProperty());
        //bind state of info button
        InfoBtn.disableProperty().bind(pvm.removeGetInfoDisabledProperty());
        //bind state of add button
        AddProductBtn.disableProperty().bind(pvm.addButtonDisabledProperty());

        //on subscribe
        SubscribeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pvm.subscribe();
            }
        });
        //on Add
        AddProductBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pvm.addProduct();
            }
        });
        //on Remove
        RemoveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int ind = Products.getSelectionModel().getSelectedIndex();
                pvm.removeProduct(ind);
            }
        });
        //on info
        InfoBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //get selected index
                int ind = Products.getSelectionModel().getSelectedIndex();
                pvm.getInfoOnProduct(ind);
            }
        });

    }
}
