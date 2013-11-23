package config;

import ViewModel.MainViewModel;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import controller.LoginController;
import controller.MainViewController;
import controller.SearchResultsController;
import javafx.application.HostServices;
import serviceclient.notification.NotificationService;
import utility.ProductTaskFactory;

import java.util.logging.FileHandler;
import java.util.logging.Handler;

/**
 * Module used by Guice to define dependencies
 */
public class MainModule extends AbstractModule {
    //URL of the notification server
    private String BASE_URL;
    //reference to the user's browser
    private HostServices hostServices;

    /**
     * Constructor
     *
     * @param BASE_URL     notification server url
     * @param hostServices reference to user's browser
     */
    public MainModule(String BASE_URL, HostServices hostServices) {
        this.BASE_URL = BASE_URL;
        this.hostServices = hostServices;
    }

    /**
     * Define dependencies
     */
    @Override
    protected void configure() {
        ServerConfig sc = new ServerConfig(BASE_URL);
        bind(Handler.class).to(FileHandler.class).in(Scopes.SINGLETON);
        bind(ServerConfig.class).toInstance(sc);
        bind(HostServices.class).toInstance(hostServices);
        bind(NotificationService.class).in(Scopes.SINGLETON);
        bind(ProductTaskFactory.class).in(Scopes.SINGLETON);
        bind(MainViewModel.class).in(Scopes.SINGLETON);
        bind(MainViewController.class);
        bind(LoginController.class);
        bind(SearchResultsController.class);
    }
}
