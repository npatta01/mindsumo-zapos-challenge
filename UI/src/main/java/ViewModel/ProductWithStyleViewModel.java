package ViewModel;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.ProductWithStyle;

import java.text.NumberFormat;

/**
 * View Model for a product
 * Wrapper around ProductWithStyle
 */
public class ProductWithStyleViewModel {
    //product
    private final ProductWithStyle p;
    //productId
    private final StringProperty productId;
    //productName
    private final StringProperty productName;
    //original Price
    private final SimpleStringProperty originalPrice;
    //url containing info in product
    private final SimpleStringProperty webURL;
    //current price
    private final SimpleStringProperty currentPrice;
    //thumbnail url
    private final SimpleStringProperty imageUrl;
    //style id
    private final SimpleStringProperty styleId;
    //is the object selected
    private SimpleBooleanProperty selected;

    /**
     * Constructor
     * @param product  product with style info
     */
    public ProductWithStyleViewModel(ProductWithStyle product) {
        p = product;
        //set to original items value
        productId = new SimpleStringProperty(String.valueOf(p.productId));
        productName = new SimpleStringProperty(p.productName);
        String currencyString = NumberFormat.getCurrencyInstance().format(p.originalPrice);
        originalPrice = new SimpleStringProperty(currencyString);
        currencyString = NumberFormat.getCurrencyInstance().format(p.price);
        currentPrice = new SimpleStringProperty(currencyString);
        webURL = new SimpleStringProperty(product.productUrl);
        imageUrl = new SimpleStringProperty(product.thumbnailImageUrl);
        styleId = new SimpleStringProperty(String.valueOf(product.styleId));
        selected = new SimpleBooleanProperty(false);
    }

    /**
     * Equals if underlying product is equal
     * @param o
     * @return true|false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductWithStyleViewModel that = (ProductWithStyleViewModel) o;
        if (!p.equals(that.p)) return false;
        return true;
    }

    /**
     * Hash code
     * @return  underlying product hash code
     */
    @Override
    public int hashCode() {
        return p.hashCode();
    }

    public final SimpleStringProperty styleIdProperty() {
        return styleId;
    }

    public final SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public Boolean getSelected() {
        return selectedProperty().get();
    }

    public void setSelected(boolean b) {
        selectedProperty().set(b);
    }

    public final SimpleStringProperty imageUrlProperty() {
        return imageUrl;
    }

    public final String getImageUrl() {
        return imageUrlProperty().get();
    }

    public final String getProductId() {
        return productId.getValueSafe();
    }

    public void setProductId(String newEmail) {
        productId.setValue(newEmail);
    }

    public final StringProperty productIdProperty() {
        return productId;
    }

    public final String getProductName() {
        return productName.getValueSafe();
    }

    public void setProductName(String newEmail) {
        productName.setValue(newEmail);
    }

    public final StringProperty productNameProperty() {
        return productName;
    }

    public final SimpleStringProperty priceProperty() {
        return currentPrice;
    }

    public final SimpleStringProperty originalPriceProperty() {
        return originalPrice;
    }


    public String getWebURL() {
        return webURL.get();
    }

    public SimpleStringProperty webURLProperty() {
        return webURL;
    }
}
