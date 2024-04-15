
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;

public class PrimaryController implements Initializable {

    @FXML // fx:id="Canceldistressbt1"
    private Button Canceldistressbt1; // Value injected by FXMLLoader

    @FXML // fx:id="LabelUser"
    private TextField LabelUser; // Value injected by FXMLLoader

    @FXML // fx:id="Log_In_button"
    private Button Log_In_button; // Value injected by FXMLLoader

    @FXML // fx:id="Password_button"
    private PasswordField Password_button; // Value injected by FXMLLoader

    @FXML // fx:id="distressbut"
    private ImageView distressbut; // Value injected by FXMLLoader

    @FXML // fx:id="passwordlabel"
    private TextField passwordlabel; // Value injected by FXMLLoader

    @FXML // fx:id="request"
    private TextField request; // Value injected by FXMLLoader

    @FXML // fx:id="title"
    private TextArea title; // Value injected by FXMLLoader

    @FXML // fx:id="txt_usrn"
    private TextField txt_usrn; // Value injected by FXMLLoader

    @FXML
    private ImageView im;

   /* Image myImage=new Image(getClass().getResourceAsStream("im.png"));
    public void displayIm(){
        im.setImage(myImage);

    }*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize method called when the controller is loaded
        title.setEditable(false);
        request.setEditable(false);
        LabelUser.setEditable(false);
        passwordlabel.setEditable(false);

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
    void CancelDistress1(ActionEvent event) {
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

    void receiveFromServer(String message) {
        // Method to handle the message/. received from the server
        if (message.equals("LOGIN_SUCCESS")) {
            // Handle login success
            System.out.println("Login successful");
        } else if (message.equals("LOGIN_FAIL")) {
            // Handle login failure
            System.out.println("Login failed");
        } else {
            // Handle other types of messages
            System.out.println("Received message: " + message);
        }
    }


    // Check username and password locally
    // Username and password are valid, sending a message to the server for further validation
//			try {
//				String messageToSend = "#LogInAttempt," + username + "@" + password;
//				// Send message to the server for additional validation
//				SimpleClient.getClient().sendToServer(messageToSend);
//				Thread.sleep(500);
//				// You might want to add further logic here based on the server response
//			} catch (IOException e) {
//				showAlert("Error", "Failed to Get Login message!" + e.getMessage());
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

    @FXML
    void Password(ActionEvent event) {

    }


    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCall");


    }
}