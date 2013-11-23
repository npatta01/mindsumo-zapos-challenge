package controller;

import ViewModel.MainViewModel;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller used for the login screen
 */
public class LoginController implements Initializable {

    //email input prompt
    @FXML
    public TextField EmailInputPrompt;
    //login button
    @FXML
    Button LoginBtn;
    //main view model
    private MainViewModel pvm;

    /**
     * Constructor
     * @param pvm MainViewModel
     */
    @Inject
    public LoginController(MainViewModel pvm) {
       this.pvm = pvm;
    }

    /**
     * Initialize controller
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //bind input prompt to view model email
        EmailInputPrompt.textProperty().bindBidirectional(pvm.emailProperty());
        //when login button is clicked, login
        LoginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pvm.login();
            }
        });
        //login button is disabled if not valid email
        LoginBtn.disableProperty().bind(pvm.validEmailProperty());
    }
}
