package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {

    @FXML // fx:id="Canceldistressbt1"
    private Button Canceldistressbt1; // Value injected by FXMLLoader

    @FXML // fx:id="LabelUser"
    private TextField LabelUser; // Value injected by FXMLLoader

    @FXML // fx:id="Log_In_button"
    private Button Log_In_button; // Value injected by FXMLLoader

    @FXML // fx:id="Password_button"
    private PasswordField Password_button; // Value injected by FXMLLoader

    @FXML // fx:id="distressRequest"
    private Button distressRequest; // Value injected by FXMLLoader

    @FXML // fx:id="im"
    private ImageView im; // Value injected by FXMLLoader

    @FXML // fx:id="passwordlabel"
    private TextField passwordlabel; // Value injected by FXMLLoader

    @FXML // fx:id="request"
    private TextField request; // Value injected by FXMLLoader

    @FXML // fx:id="title"
    private TextArea title; // Value injected by FXMLLoader

    @FXML // fx:id="txt_usrn"
    private TextField txt_usrn; // Value injected by FXMLLoader


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title.setEditable(false);
        request.setEditable(false);
        LabelUser.setEditable(false);
        passwordlabel.setEditable(false);
    }
    @FXML
    void distressRequest(ActionEvent event) throws IOException {
          App.setRoot("distressCall");
    }

    @FXML
    void sendWarning(ActionEvent event) {
        try {
            SimpleClient.getClient().sendToServer("#warning");
        } catch (IOException e) {
            showAlert("Error", "Failed to send warning: " + e.getMessage());
        }
    }

    @FXML
    void User_Name(ActionEvent event) {
    }

    private void showAlert(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void ClientProfileLoad(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("clientHomePage.fxml"));
            AnchorPane newScene = loader.load();

            Stage currentStage = App.getStage();
            Scene scene = new Scene(newScene);
            currentStage.setTitle("Client Home Page");
            currentStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void Log_In(ActionEvent event) throws IOException {
        String username = txt_usrn.getText();
        String password = Password_button.getText();
        System.out.println(username);
        System.out.println(password);
        try {
            String messageToSend = "#LogInAttempt," + username + "@" + password;

            // Send message to the server for validation
            SimpleClient.getClient().sendToServer(messageToSend);

            // Receive response from the server
        } catch (IOException e) {
            showAlert("Error", "Failed to Get Login message!" + e.getMessage());
            e.printStackTrace();
            System.out.println("LOGIN_FAIL");
        }
    }

    @FXML
    void Password(ActionEvent event) {

    }

    @FXML
    void distress(ActionEvent event) {

    }
}