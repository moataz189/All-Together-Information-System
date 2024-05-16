package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.UserControl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;

public class DistressCallsecondary {

    @FXML
    private Button Ambulancebtn;

    @FXML
    private Button Back;

    @FXML
    private Button FireBtn;

    @FXML
    private ChoiceBox<String> Location;

    @FXML
    private TextField userId;

    @FXML
    private TextField userName;

    @FXML
    private Button policebtn;

    @FXML
    private ImageView im1;

    @FXML
    private ImageView im2;

    @FXML
    private ImageView im3;

    private static User userLogIn;
    @FXML
    void initialize() {

        // Initialize the ChoiceBox with choices
        Location.getItems().add("Yaffa Nazareth");
        Location.getItems().add("Kabul");
        Location.getItems().add("Nazareth");
        Location.getItems().add("Tamra");
        Location.getItems().add("Kukab");
        Location.getItems().add("UMM El Fahem");
        ArrayList<User> loggedInList = UserControl.getLoggedInList();
        if (!loggedInList.isEmpty()) {
            User lastUser = loggedInList.get(loggedInList.size() - 1);
            userName.setText(lastUser.getFirstName() + " " + lastUser.getLastName());
            userId.setText(lastUser.getID());
            userLogIn = lastUser;
        }
        userId.setEditable(false);
        userName.setEditable(false);
    }

    @FXML
    void Back(ActionEvent event) throws IOException {
        if(Managercontrol.getManagerLogIn()==null)
           App.setRoot("secondary");
        else App.setRoot("manager_control");
    }

    @FXML
    void savecall(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        if (Location.getValue() == null) {
            //SimpleClient.getClient().sendToServer("is not Registered");
            System.out.println("Wrong");
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                alert.setTitle("Request Error"); // Set the window's title
                alert.setHeaderText(null); // Optional: you can have a header or set it to null
                alert.setContentText("Your Request is denied,please select your Location"); // Set the main message/content
                alert.showAndWait(); // Display the alert and wait for the user to close it
            });
        } else {


            try {
                ArrayList<User> loggedInList = UserControl.getLoggedInList();
                User lastUser = loggedInList.get(loggedInList.size() - 1);
                String x = lastUser.getID();
                System.out.println(x);
                String stringValue = String.valueOf(x);
                SimpleClient.getClient().sendToServer("2The key id is:" + stringValue + "@" + buttonText + "@" + Location.getValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
