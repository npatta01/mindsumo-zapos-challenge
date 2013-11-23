package model;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import serviceclient.zappos.MoneyDeserializer;
import serviceclient.zappos.PercentDeserializer;

public class ProductWithStyle {
    //style id
    public long styleId;
    //color
    public String color;
    //original price
    @JsonDeserialize(using = MoneyDeserializer.class)
    public float originalPrice;
    @JsonDeserialize(using = MoneyDeserializer.class)
    //current price
    public float price;
    //percent off
    @JsonDeserialize(using = PercentDeserializer.class)
    public float percentOff;
    //product url
    public String productUrl;
    //image url
    public String imageUrl;
    //thumbnail url
    public String thumbnailImageUrl;
    //product id
    public long productId;
    //product name
    public String productName;

    /**
     * Constructor
     * Set url to empty
     */
    public ProductWithStyle() {
        thumbnailImageUrl = "";
        imageUrl = "";
    }

    /**
     * Two objects are equal if they have same product and style id
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductWithStyle that = (ProductWithStyle) o;
        if (productId != that.productId) return false;
        return styleId == that.styleId;
    }

    /**
     * Hash code
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = (int) (styleId ^ (styleId >>> 32));
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        return result;
    }
}