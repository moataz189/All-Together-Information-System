package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.UserControl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SecondaryController implements Initializable {

    @FXML
    private Button confim_vol;

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
    private ImageView im;
    @FXML
    private TextField userId;

    @FXML
    private TextField userName;

    private static User userLogIn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public static User getUserLogIn() {
        return userLogIn;
    }

    @FXML
    void message(ActionEvent event) throws IOException {
        try {
            Object[] array = new Object[2];
            array[0] = "Get uploaded messages"; // Assign a String object to the first index
            array[1] = SecondaryController.getUserLogIn();
            SimpleClient.getClient().sendToServer(array);

        } catch (IOException e) {
            showAlert("Error", "Failed to get uploaded community tasks: " + e.getMessage());
            e.printStackTrace();
        }
     /*   try {
            SimpleClient.getClient().sendToServer("Get all users");
        } catch (IOException e) {
            showAlert("Error", "Failed to get uploaded community tasks: " + e.getMessage());
            e.printStackTrace();
        }*/

    }

    @FXML
    void CANCELREQUST(ActionEvent event) {
        try {
            Object[] array = new Object[2];
            array[0] = "Cancel request"; // Assign a String object to the first index
            array[1] = SecondaryController.getUserLogIn();
            SimpleClient.getClient().sendToServer(array);
        } catch (IOException e) {
            showAlert("Error", "Failed to get your service requests: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void Confirm(ActionEvent event) {
        try {
            Object[] array = new Object[2];
            array[0] = "Confirm Volunteer"; // Assign a String object to the first index
            array[1] = SecondaryController.getUserLogIn();
            SimpleClient.getClient().sendToServer(array);
        } catch (IOException e) {
            showAlert("Error", "Failed to confirm your volunteer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void LOGOUT(ActionEvent event) throws IOException {
        SimpleClient.getClient().sendToServer("log out " + getUserLogIn().getID());
        Platform.runLater(() -> {
            try {
                App.setRoot("primary");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void Requesthelp(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                App.setRoot("request_control");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        SimpleClient.getClient().sendToServer("Create task");
    }

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }

    @FXML
    void volunter(ActionEvent event) throws IOException {
        try {
            SimpleClient.getClient().sendToServer("getVolunteerTasks");
        } catch (IOException e) {
            showAlert("Error", "Failed to Get Login message!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void showAlert(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
