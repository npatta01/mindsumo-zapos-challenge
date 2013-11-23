package utility;

import ViewModel.ProductWithStyleViewModel;
import com.google.inject.Inject;
import javafx.concurrent.Task;
import model.ProductWithStyle;
import serviceclient.notification.NotificationService;

/**
 * Factory that creates task that contact api
 */
public class ProductTaskFactory {
    @Inject
    private NotificationService ns;

    /**
     * Return search results for product query
     *
     * @param term query term
     * @return search results
     */
    public Task<ProductWithStyle[]> getProduct(final String term) {
        return new Task<ProductWithStyle[]>() {
            @Override
            protected ProductWithStyle[] call() throws Exception {
                //if numeric, it is a product id
                boolean isNumeric;
                try {
                    Long id = Long.parseLong(term);
                    isNumeric = true;
                } catch (NumberFormatException nfe) {
                    isNumeric = false;
                }
                ProductWithStyle[] productByProdcutId;
                if (isNumeric) {//product id
                    productByProdcutId = ns.getProductByProdcutId(term);
                } else {      //product name
                    productByProdcutId = ns.getProductByName(term);
                }
                return productByProdcutId;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    /**
     * Get a product by style id
     *
     * @param id style id
     * @return Style
     */
    public Task<ProductWithStyle> getProductByStyle(final String id) {
        return new Task<ProductWithStyle>() {
            @Override
            protected ProductWithStyle call() throws Exception {
                ProductWithStyle[] productByProdcutId;
                productByProdcutId = ns.getProductByStyleId(id);
                if (productByProdcutId.length == 1) {
                    return productByProdcutId[0];
                }
                return null;
            }
        };
    }

    /**
     * Register the user with system and fetch subscriptions
     *
     * @param email email Address
     * @return Subscribed items
     */
    public Task<ProductWithStyle[]> registerAndGetRegisteredProducts(final String email) {
        return new Task<ProductWithStyle[]>() {
            @Override
            protected ProductWithStyle[] call() throws Exception {
                ProductWithStyle[] subscribedProducts = ns.register(email);
                return subscribedProducts;
            }
        };
    }

    /**
     * Synchronize item list with server
     * @param email                     email address
     * @param items                                  items
     * @return                                         void
     */
    public Task synchronize(final String email, final ProductWithStyleViewModel[] items) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (items != null) {
                    Long[] ids = new Long[items.length];
                    for (int i = 0; i < items.length; i++) {
                        ids[i] = Long.valueOf(items[i].styleIdProperty().get());
                    }
                    ns.notify(email, ids);
                }
                return null;
            }
        };
    }
}
