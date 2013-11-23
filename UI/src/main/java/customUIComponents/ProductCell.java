package customUIComponents;

import ViewModel.ProductWithStyleViewModel;
import controller.ProductCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.InputStream;

/**
 * A Custom ListViewCell that displays a Product with style info
 */
public class ProductCell extends ListCell<ProductWithStyleViewModel> {
    public static final String PRODUCT_CELL_FXML = "/ProductCell.fxml";

    @Override
    public void updateItem(ProductWithStyleViewModel item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                ProductCellController pcc = new ProductCellController(item);
                fxmlLoader.setController(pcc);
                InputStream in =
                        getClass().getResourceAsStream(PRODUCT_CELL_FXML);
                Node root = (Node) fxmlLoader.load(in);
                setGraphic(root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


