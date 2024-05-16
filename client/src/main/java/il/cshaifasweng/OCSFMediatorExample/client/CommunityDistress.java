package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DistressCall;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CommunityDistress {
    @FXML
    private Button tasks;
    @FXML
    private ListView<String> calls;
    public static List<DistressCall> getCommunitycalls = new ArrayList<>();
    private DistressCall requestedcall = null;

    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }
    public void initialize() {
        if (getCommunitycalls.isEmpty()) {
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There are no calls in your community.");
                try {
                    App.setRoot("manager_control");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return;
        }
        while (getCommunitycalls.isEmpty()) {
            try {
                Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (DistressCall call : getCommunitycalls) {
            this.calls.getItems().addAll("call Id: " + call.getId() + " - " + call.getEmergencyCenter().getService() + "- " + call.getDate());
        }
        this.calls.setOnMouseClicked(event -> {
            String selectedTaskName = this.calls.getSelectionModel().getSelectedItem();
            if (selectedTaskName != null) {
                // Split the selected item based on the delimiter " - "
                String[] parts = selectedTaskName.split(" - ");
                if (parts.length > 0) {
                    // Extract the user ID from the first part of the split string
                    String userIdString = parts[0].trim();
                    // Remove the "User Id: " prefix to get the actual user ID
                    String userId = userIdString.substring("call Id: ".length());
                    // Parse the user ID string into an integer
                    long number = Long.parseLong(userId);

                    // Iterate through the list of users
                    for (DistressCall call : getCommunitycalls) {
                        // Check if the user ID matches the selected user ID
                        if (call.getId().equals(number)) {
                            // Found the selected user, assign it to selectedUser and break the loop
                            requestedcall = call;
                            break;
                        }
                    }
                }
            }
        });
    }

    private void showCompletionMessage(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Use INFORMATION type for completion message
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Back(ActionEvent event) throws IOException {
        App.setRoot("Distresscalloption");
    }

    @FXML
    void callsDetails(ActionEvent event) {
        System.out.println("4");
        if (requestedcall != null) {
            System.out.println("5");
            Long id = requestedcall.getId();
            String name = requestedcall.getUser().getFirstName() + " " + requestedcall.getUser().getLastName();
            System.out.println(name);
            String community = requestedcall.getUser().getCommunity();
            String address = requestedcall.getEmergencyCenter().getService();
            LocalDate callDate = requestedcall.getDate();
            LocalTime callTime = requestedcall.getTime();
            String details = String.format("Call ID: %s\nUser Name: %s\nCommunity: %s\nService: %s\nDate: %s\nTime: %s",
                    id, name, community, address, callDate.toString(), callTime.toString());

            showAlert(details);
        }
    }

    private void showAlert(String details) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Call Details");
        alert.setHeaderText("Call Details:");
        alert.setContentText(details);
        alert.showAndWait();
    }
}