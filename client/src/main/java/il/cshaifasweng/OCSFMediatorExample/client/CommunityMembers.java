package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommunityMembers {

    @FXML // fx:id="back"
    private Button back; // Value injected by FXMLLoader
    @FXML
    private ImageView image;

    @FXML // fx:id="member"
    private Button member; // Value injected by FXMLLoader

    @FXML // fx:id="memberList"
    private ListView<String> memberList; // Value injected by FXMLLoader

    public static List<User> users = new ArrayList<>();
    private User selectedUser = null;
    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }

    public void initialize() {
        if (users.isEmpty()) {
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There are no members in your community.");
                try {
                    App.setRoot("manager_control");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return;
        }
        while (users.isEmpty()) {
            try {
                Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (User user : users) {
            this.memberList.getItems().addAll("User Id: "+ user.getID()+ " - " +user.getFirstName() + " " + user.getLastName());
        }
        this.memberList.setOnMouseClicked(event -> {
            String selectedTaskName = this.memberList.getSelectionModel().getSelectedItem();
            if (selectedTaskName != null) {
                // Split the selected item based on the delimiter " - "
                String[] parts = selectedTaskName.split(" - ");
                if (parts.length > 0) {
                    // Extract the user ID from the first part of the split string
                    String userIdString = parts[0].trim();
                    // Remove the "User Id: " prefix to get the actual user ID
                    String userId = userIdString.substring("User Id: ".length());
                    // Parse the user ID string into an integer

                    // Iterate through the list of users
                    for (User user : users) {
                        // Check if the user ID matches the selected user ID
                        if (user.getID().equals( userId)) {
                            // Found the selected user, assign it to selectedUser and break the loop
                            selectedUser = user;
                            break;
                        }
                    }
                }
            }
        });
    }

    @FXML
    void memberDetails(ActionEvent event) throws IOException {
        if (selectedUser != null) {
            String id = selectedUser.getID();
            String name = selectedUser.getFirstName() + " " + selectedUser.getLastName();
            String community = selectedUser.getCommunity();
            String address = selectedUser.getAddress();
            String email = selectedUser.getEmail();
            String x = String.format("User ID: %s\nUser Name: %s\nCommunity: %s\nAddress: %s\nEmail: %s", id, name, community, address, email);
            showAlert(x);
        }
    }

    @FXML
    void previous(ActionEvent event) throws IOException {
        App.setRoot("manager_control");
    }

    private void showAlert(String task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User details");
        alert.setHeaderText("User Details: ");
        alert.setContentText(task);
        alert.showAndWait();
    }

    private void showCompletionMessage(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Use INFORMATION type for completion message
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}