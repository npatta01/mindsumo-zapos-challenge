package utility;

import controller.LoginController;
import controller.MainViewController;
import controller.SearchResultsController;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * A helper class used to launch different windows
 */
public class ViewManager {
    //reference to the FXML loader
    private static GuiceFXMLLoader guiceFXMLLoader;
    //main stage
    private static Stage primaryStage;
    //child stage
    private static Stage childStage;

    /**
     * Set the fxml loader
     *
     * @param g  reference to fxml loader
     */
    public static void setFxmlLoader(GuiceFXMLLoader g) {
        guiceFXMLLoader = g;
    }

    /**
     * Save reference to the primary stage
     *
     * @param stage  reference to primary stage
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Show a window displaying the different styles for the given product
     *
     * @param product //query term
     */
    public static void displayMultipleStyleWindow(String product) {
        childStage = new Stage();
        Parent root = (Parent) guiceFXMLLoader.load("/SearchResults.fxml", SearchResultsController.class);
        childStage.setTitle("Multiple results for product:" + product);
        childStage.initModality(Modality.WINDOW_MODAL);
        childStage.initStyle(StageStyle.UTILITY);
        childStage.setScene(new Scene(root));
        childStage.show();
    }

    /**
     * Display login window
     */
    public static void displayLoginWindow() {
        childStage = new Stage();
        childStage.initOwner(primaryStage);
        childStage.initModality(Modality.WINDOW_MODAL);
        Parent root = (Parent) guiceFXMLLoader.load("/LoginView.fxml", LoginController.class);
        childStage.setTitle("Zappos Notification Service");
        childStage.initModality(Modality.WINDOW_MODAL);
        childStage.setScene(new Scene(root));
        childStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                primaryStage.close();
            }
        });
        childStage.show();
    }

    public static void showMainView() {
        // Ask to load the Sample.fxml file, injecting an instance of a SampleController
        Parent root = (Parent) guiceFXMLLoader.load("/MainView.fxml", MainViewController.class);
        primaryStage.setTitle("Zappos Notification Service");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void hideChildView() {
        childStage.close();
        childStage = null;
    }
}
