import com.google.inject.Guice;
import com.google.inject.Injector;
import config.MainModule;
import javafx.application.Application;
import javafx.stage.Stage;
import utility.GuiceFXMLLoader;
import utility.ViewManager;

/**
 * Entry Point for the Gui
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //url of the notification service
        String BaseURL = "http://fakezapposnotificationservice.npatta01.cloudbees.net/api";
        //BaseURL = "http://localhost:9000/api";
        Injector injector = Guice.createInjector(new MainModule(BaseURL, getHostServices()));
        // Create a new Guice-based FXML Loader
        GuiceFXMLLoader loader = new GuiceFXMLLoader(injector);
        //set the fxml loader and the primary stage
        ViewManager.setFxmlLoader(loader);
        ViewManager.setPrimaryStage(primaryStage);

        //show main window and then the login modal
        ViewManager.showMainView();
        ViewManager.displayLoginWindow();
    }
}
