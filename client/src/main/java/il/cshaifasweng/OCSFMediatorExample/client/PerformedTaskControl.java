
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerformedTaskControl {

    @FXML // fx:id="back"
    private Button back; // Value injected by FXMLLoader

    @FXML // fx:id="communityTasks"
    private ListView<String> communityTasks; // Value injected by FXMLLoader

    @FXML // fx:id="tasks"
    private Button tasks; // Value injected by FXMLLoader

    public static List<Task> doneTasks =new ArrayList<>();
    private Task requestedTask = null;

    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }
    private void showCompletionMessage(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Use INFORMATION type for completion message
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initialize(){
        if (doneTasks.isEmpty()) {
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There are no performed tasks in your community.");
                try {
                    App.setRoot("manager_control");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return;
        }

        while (doneTasks.isEmpty()) {
            try {
                Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for(Task task : doneTasks){
            this.communityTasks.getItems().addAll(task.getServiceType());
        }
        this.communityTasks.setOnMouseClicked(event -> {
            String selectedTaskName = this.communityTasks.getSelectionModel().getSelectedItem();
            if(selectedTaskName!=null){
                for(Task task : doneTasks){
                    if(task.getServiceType().equals(selectedTaskName)){
                        requestedTask = task;
                        break;
                    }
                }
            }
        });
    }

    private void showAlert(String task){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task details");
        alert.setHeaderText("Task Details: " );
        alert.setContentText(task);
        alert.showAndWait();
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

    @FXML
    void tasksDetails(ActionEvent event) {
        if(requestedTask != null){
            int id= (int) requestedTask.getIdNum();
            String serviceType= requestedTask.getServiceType();
            String fitst=requestedTask.getUser().getFirstName();
            String userid=requestedTask.getUser().getID();
            int status=requestedTask.getStatus();
            String volId = requestedTask.getVolId();
            String x = String.format("Task ID: %d\nTask Description: %s\nUser Name: %s\nUser ID: %s\nVolunteer Id: %s\nStatus: %d", id, serviceType, fitst, userid,volId,status);
            showAlert(x);
        }
    }
}
