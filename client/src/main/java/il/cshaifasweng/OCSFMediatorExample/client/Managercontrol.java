/**
 * Sample Skeleton for 'manager_control.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.UserControl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class Managercontrol {

    @FXML
    private Button distress;

    @FXML
    private Button doneTasks;

    @FXML
    private TextField id;

    @FXML
    private Button members;

    @FXML
    private TextField name;

    @FXML
    private Button requests;

    @FXML
    private Button serviceRequest;

    @FXML
    private Button updateTask;

    @FXML
    private Button volunteer;

    @FXML
    private Button DistressButtonControl;



    private static User managerLogIn;

    public void initialize() {

        ArrayList<User> loggedInList = UserControl.getLoggedInList();
        if (!loggedInList.isEmpty()) {
            User lastUser = loggedInList.get(loggedInList.size() - 1);
            name.setText(lastUser.getFirstName() + " " + lastUser.getLastName());
            id.setText(lastUser.getID());
            managerLogIn = lastUser;
        }
        id.setEditable(false);
        name.setEditable(false);
    }
    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }
    @FXML
    void getDistressCalls(ActionEvent event) throws IOException {
        App.setRoot("Distresscalloption");
    }

    // Getter for userLogIn
    public static User getManagerLogIn() {
        return managerLogIn;
    }

    // Setter for userLogIn
    public static void setUserLogIn(User user) {
        managerLogIn = user;
    }

    @FXML
    void communityRequests(ActionEvent event) {
        try {
            SimpleClient.getClient().sendToServer("Get uploaded tasks by community members@"+managerLogIn.getCommunityManager());
        } catch (IOException e) {
            showAlert("Error", "Failed to get uploaded community tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void getCommunityMembers(ActionEvent event) throws IOException {
        try {
            System.out.println(managerLogIn.getCommunityManager());
            SimpleClient.getClient().sendToServer("Get community members@"+managerLogIn.getCommunityManager());
        } catch (IOException e) {
            showAlert("Error", "Failed to get community members: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    void getDoneTasks(ActionEvent event) {
        try {
            System.out.println("yessss");
            System.out.println(managerLogIn.getCommunityManager());
            SimpleClient.getClient().sendToServer("Get performed tasks@"+managerLogIn.getCommunityManager());
        } catch (IOException e) {
            showAlert("Error", "Failed to get the performed tasks: " + e.getMessage());
            e.printStackTrace();
        }
       /* try {
            Object[] array = new Object[2];
            array[0] = "Get maneger messages"; // Assign a String object to the first index
            array[1] = managerLogIn.getID();
            SimpleClient.getClient().sendToServer(array);

        } catch (IOException e) {
            showAlert("Error", "Failed to get the performed tasks: " + e.getMessage());
            e.printStackTrace();
        }*/
    }

    @FXML
    void showServiceRequests(ActionEvent event) {
        //to check the requests.
        try {
            SimpleClient.getClient().sendToServer("check requests@"+managerLogIn.getCommunityManager());
        } catch (IOException e) {
            showAlert("Error", "Failed to get community help requests: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void updateTaskDetails(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                App.setRoot("updateTaskDetails");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        SimpleClient.getClient().sendToServer("log out " + getManagerLogIn().getID());
        Platform.runLater(() -> {
            try {
                App.setRoot("primary");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}