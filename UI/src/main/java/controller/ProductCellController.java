package controller;

import ViewModel.ProductWithStyleViewModel;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for a single listview displaying a product
 */
public class ProductCellController implements Initializable {
    @FXML
    private ImageView imageView;
    @FXML
    private Label ProductNameLabel;
    @FXML
    private Label StyleIdLabel;
    @FXML
    private Label SalePriceLabel;
    @FXML
    private Label OriginalPriceLabel;
    @FXML
    private CheckBox SelectedCheckBox;
    @FXML
    private Label ProductIdLabel;

    //reference to the main view model
    private ProductWithStyleViewModel pw;

    @Inject
    public ProductCellController(ProductWithStyleViewModel pw) {
        this.pw = pw;
    }

    /**
     * Initialize the product cell
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set image to thumbnail url
        imageView.setImage(new Image(pw.imageUrlProperty().get(), 75, 75, true, true, true));
        //set product name
        ProductNameLabel.setText(pw.getProductName());
        //set style id
        StyleIdLabel.setText(pw.styleIdProperty().get());
        //set sales price
        SalePriceLabel.setText(pw.priceProperty().get());
        //set original price
        OriginalPriceLabel.setText(pw.originalPriceProperty().get());
        //set product id
        ProductIdLabel.setText(pw.productIdProperty().get());
        //set selected box
        SelectedCheckBox.selectedProperty().bindBidirectional(pw.selectedProperty());
    }
}
