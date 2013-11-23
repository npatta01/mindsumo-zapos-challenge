package utility;

import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;

import java.io.InputStream;

//Class taken from external source
//http://fxexperience.com/2011/10/fxml-guice/

/**
 * Uses Guice to inject model state. Basically you create an instance of
 * GuiceFXMLLoader supplying an Injector, and then call load. The load
 * method takes the FXML file to load, and the controller to create and
 * associate with the FXML file.
 *
 * http://fxexperience.com/2011/10/fxml-guice/
 */
public class GuiceFXMLLoader {
    private final Injector injector;

    @Inject
    public GuiceFXMLLoader(Injector injector) {
        this.injector = injector;
    }

    // Load some FXML file, using the supplied Controller, and return the
    // instance of the initialized controller...?
    public Object load(String url, Class<?> controller) {
        Object instance = injector.getInstance(controller);
        FXMLLoader loader = new FXMLLoader();
        //loader.getNamespace().put("controller", instance);
        loader.setController(instance);
        try {
            InputStream in =
                    getClass().getResourceAsStream(url);
            return loader.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}