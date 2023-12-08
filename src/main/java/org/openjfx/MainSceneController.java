package org.openjfx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class MainSceneController {

    @FXML
    private Button loginButton;

    @FXML
    void btnLogin(ActionEvent event) {
        System.out.println("Login succesful!");
    }

}