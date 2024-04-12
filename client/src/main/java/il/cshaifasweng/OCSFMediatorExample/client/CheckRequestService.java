package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;

public class CheckRequestService {

    @FXML // fx:id="back"
    private Button back; // Value injected by FXMLLoader

    @FXML // fx:id="communityTasks"
    private ListView<String> communityTasks; // Value injected by FXMLLoader

    @FXML // fx:id="tasks"
    private Button tasks; // Value injected by FXMLLoader
    @FXML
    private Button acceptbtn;
    @FXML
    private Button removebtn;

    public static List<Task> requests =new ArrayList<>();
    private static Task requestedTask = null;
    public void initialize() {
        if (requests.isEmpty()) {
            // If requests list is empty, do nothing
            return;
        }
        // Add items to the ListView
        for (Task task : requests) {
            this.communityTasks.getItems().add(task.getServiceType());
        }
        // Set event handler for mouse click on ListView
        this.communityTasks.setOnMouseClicked(event -> {
            String selectedTaskName = this.communityTasks.getSelectionModel().getSelectedItem();
            if (selectedTaskName != null) {
                // Find the selected task in the requests list
                for (Task task : requests) {
                    if (task.getServiceType().equals(selectedTaskName)) {
                        requestedTask = task;
                        break;
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
    private void showAlert(String task){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task details");
        alert.setHeaderText("Task Details: " );
        alert.setContentText(task);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void accept(ActionEvent event) throws IOException {
        if(requestedTask != null){
            int id= requestedTask.getIdNum();
            System.out.println(id);
            String message = "Task is Accept@" + String.valueOf(id) + "@" + "0";
            SimpleClient.getClient().sendToServer(message);
        }
        try {
            showCompletionMessage("Request accepted", "Thanks. \nThe request accepted successfully- Id request "+ requestedTask.getIdNum());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            SimpleClient.getClient().sendToServer("check requests");
        } catch (IOException e) {
            showAlert("Error", "Failed to get community help requests: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    void remove(ActionEvent event) throws IOException {
        if(requestedTask != null) {
            App.setRoot("Reject_control");
        }

    }
    public static   Task getRequestedTask() {
        return requestedTask;
    }

    // Setter for requestedTask
    public void setRequestedTask(Task requestedTask) {
        this.requestedTask = requestedTask;
    }
    @FXML
    void previous(ActionEvent event) throws IOException {
        App.setRoot("manager_control");
    }

    @FXML
    void tasksDetails(ActionEvent event) {
        if(requestedTask != null){
            int id= requestedTask.getIdNum();
            String serviceType= requestedTask.getServiceType();
            String fitst=requestedTask.getUser().getFirstName();
            String userid=requestedTask.getUser().getID();
            int status=requestedTask.getStatus();
            String x = String.format("Task ID: %d\nTask Description: %s\nUser Name: %s\nUser ID: %s\nState: %d", id, serviceType, fitst, userid, status);
            showAlert(x);
        }
    }
}
//        if(requestedTask != null){
//            int id= requestedTask.getIdNum();
//            System.out.println(id);
//            String message = "Task is reject@" + String.valueOf(id) + "@" + "0";
//            SimpleClient.getClient().sendToServer(message);
//        }
//        try {
//            showCompletionMessage("Request rejected", "Thanks. \nThe request rejected - Id request "+ requestedTask.getIdNum());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            SimpleClient.getClient().sendToServer("check requests");
//        } catch (IOException e) {
//            showAlert("Error", "Failed to get community help requests: " + e.getMessage());
//            e.printStackTrace();
//        }