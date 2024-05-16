package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//class for the community submitted requests.
public class CommunityTaskControl {
    @FXML
    private TextField title;

    @FXML
    private Button submitter;

    @FXML // fx:id="back"
    private Button back; // Value injected by FXMLLoader

    @FXML // fx:id="communityTasks"
    private ListView<String> communityTasks; // Value injected by FXMLLoader

    @FXML // fx:id="tasks"
    private Button tasks; // Value injected by FXMLLoader
    @FXML
    private ImageView im;

    @FXML
    private Button refresh;

    @FXML
    private TextArea note;

    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }

    public static List<Task> getCommunityTask = new ArrayList<>();
    private Task requestedTask = null;

    public void initialize() {
        note.setEditable(false);
        if (getCommunityTask.isEmpty()) {
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There are no uploaded requests.");
                try {
                    App.setRoot("manager_control");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return;
        }
        // Add items to the ListView
        for (Task task : getCommunityTask) {
            this.communityTasks.getItems().add("Task id:" + task.getIdNum() + " - " + task.getServiceType());
        }
        // Set event handler for mouse click on ListView
        this.communityTasks.setOnMouseClicked(event -> {

            String selectedTaskName = this.communityTasks.getSelectionModel().getSelectedItem();
            if (selectedTaskName != null) {
                // Find the selected task in the getCommunityTask list
                for (Task task : getCommunityTask) {
                    String[] parts1 = selectedTaskName.split("-");
                    String[] parts2 = parts1[0].split(":");
                    // Trim the string before parsing it as an integer
                    int number = Integer.parseInt(parts2[1].trim());
                    if (task.getIdNum() == number) {
                        requestedTask = task;
                        break;
                    }
                }
            }
        });
    }


    private void showAlert(String task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task details");
        alert.setHeaderText("Task Details: ");
        alert.setContentText(task);
        alert.showAndWait();
    }

    private void showAlert2(String task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User details");
        alert.setHeaderText("User Details: ");
        alert.setContentText(task);
        alert.showAndWait();
    }

    @FXML
    void submitterDetails(ActionEvent event) {
        if (requestedTask != null) {
            String id = requestedTask.getUser().getID();
            String name = requestedTask.getUser().getFirstName() + " " + requestedTask.getUser().getLastName();
            String community = requestedTask.getUser().getCommunity();
            String address = requestedTask.getUser().getAddress();
            String email = requestedTask.getUser().getEmail();
            String x = String.format("User ID: %s\nUser Name: %s\nCommunity: %s\nAddress: %s\nEmail: %s", id, name, community, address, email);
            showAlert2(x);
        }
    }

    @FXML
    void previous(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                App.setRoot("manager_control");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Refresh(ActionEvent event) {
        try {
            SimpleClient.getClient().sendToServer("Get uploaded tasks by community members@"+Managercontrol.getManagerLogIn().getCommunityManager());
        } catch (IOException e) {
            showAlert("Error", "Failed to get uploaded community tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    void tasksDetails(ActionEvent event) {
        if(requestedTask != null){
            int id= requestedTask.getIdNum();
            String serviceType= requestedTask.getServiceType();
            String fitst=requestedTask.getUser().getFirstName();
            String userid=requestedTask.getUser().getID();
            int status=requestedTask.getStatus();
            String note= requestedTask.getNote();
            // Retrieve date and time from the requestedTask object
            LocalDate date = requestedTask.getDate();
            LocalTime time = requestedTask.getTime();

            // Format date and time strings
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            String x = String.format("Task ID: %d\nTask Description: %s\nUser Name: %s\nUser ID: %s\nStatus: %d\nDate: %s\nTime: %s\nNote: %s",
                    id, serviceType, fitst, userid, status, formattedDate, formattedTime, note);
            showAlert(x);
        }
    }

    private void showCompletionMessage(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Use INFORMATION type for completion message
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}