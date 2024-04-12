package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.UserControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.prefs.Preferences;

import static java.lang.Thread.sleep;

public class SecondaryController {


    @FXML
    private Button CANCEL_VOLUNTERINGbt;

    @FXML
    private Button Canceldistressbt;

    @FXML
    private Button DistressButtonControl;

    @FXML
    private Button LOGOUTBUTTON;

    @FXML
    private Button Requesthelpbutton;

    @FXML
    private Button VOLUNTERBUTTON;

    @FXML
    private Button cansel_REQUESTBT;
    @FXML
    private Button message;

    @FXML
    void message(ActionEvent event) throws IOException {

        try {
            SimpleClient.getClient().sendToServer("Get uploaded messages");
        } catch (IOException e) {
            showAlert("Error", "Failed to get uploaded community tasks: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            SimpleClient.getClient().sendToServer("Get all users");
        } catch (IOException e) {
            showAlert("Error", "Failed to get uploaded community tasks: " + e.getMessage());
            e.printStackTrace();
        }


    }


    @FXML
    void CANCELREQUST(ActionEvent event) {

    }

    @FXML
    void CancelDistress(ActionEvent event) {

    }

    @FXML
    void Cancelvoluntering(ActionEvent event) {

    }

    @FXML
    void LOGOUT(ActionEvent event) throws IOException {
        App.setRoot("primary");
    }

    @FXML
    void Requesthelp(ActionEvent event) throws IOException {
        App.setRoot("request_control");
        SimpleClient.getClient().sendToServer("Create task");
    }

    @FXML
    void distress(ActionEvent event) {

    }

    @FXML
    void volunter(ActionEvent event) throws IOException {
        // Retrieving the username and password
//        Preferences prefs = Preferences.userNodeForPackage(SecondaryController.class);
//        String savedUsername = prefs.get("username", "");
//        String savedPassword = prefs.get("password", "");
//        System.out.println(savedPassword);
//        System.out.println(savedUsername);
        try {
            SimpleClient.getClient().sendToServer("get tasks");
        } catch (IOException e) {
            showAlert("Error", "Failed to Get Login message!" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
