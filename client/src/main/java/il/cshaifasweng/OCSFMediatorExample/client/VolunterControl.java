package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VolunterControl {

    @FXML
    private Button PICK_TASK_BTN;

    @FXML
    private Button Task_Details_btn;

    @FXML
    private ListView<String> TasksList;

    @FXML
    private Button homepagebtn;

    public static List<Task> tasks =new ArrayList<>();
    private Task selectedTask = null;
    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress_request(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }
    public void initialize() {
        EventBus.getDefault().register(this);
        if (tasks.isEmpty()) {
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There are no requests accepted for volunteering.");
                try {
                    App.setRoot("secondary");
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
            });
            return;
        }

        // Add items to the ListView
        for (Task task : tasks) {
            this.TasksList.getItems().addAll("Task id:" + task.getIdNum() + " - " + task.getServiceType());
        }

        // Set event handler for mouse click on ListView
        this.TasksList.setOnMouseClicked(event -> {

            String selectedTaskName = this.TasksList.getSelectionModel().getSelectedItem();
            if (selectedTaskName != null) {
                // Find the selected task in the getCommunityTask list
                for (Task task :tasks) {
                    String[] parts1 = selectedTaskName.split("-");
                    String[] parts2 = parts1[0].split(":");
                    // Trim the string before parsing it as an integer
                    int number = Integer.parseInt(parts2[1].trim());
                    if (task.getIdNum() == number) {
                        selectedTask = task;
                        break;
                    }
                }
            }
        });
    }


    public void showCompletionMessage(String title, String message) {
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
    @Subscribe
    public void updateList(List<Task> listOfTasks){
        System.out.println("update list at vol: " + listOfTasks.size());
        Platform.runLater(() -> {
            this.TasksList.getItems().removeAll();
            for(Task task: tasks){
                this.TasksList.getItems().remove(task.getServiceType());
            }
            tasks.clear();
            tasks = listOfTasks;
            for (Task task : tasks) {
                this.TasksList.getItems().addAll(task.getServiceType());
            }
            this.TasksList.setOnMouseClicked(event -> {
                String selectedTaskName = this.TasksList.getSelectionModel().getSelectedItem();
                if (selectedTaskName != null) {
                    // Find the selected task in the tasks list
                    for (Task task : tasks) {
                        if (task.getServiceType().equals(selectedTaskName)) {
                            selectedTask = task;
                            break;
                        }
                    }
                }
            });
        });
    }

    @FXML
    void PICK_TASK(ActionEvent event)throws IOException {
        if(selectedTask != null) {
            Alert alert = new Alert((Alert.AlertType.INFORMATION));
            alert.setTitle("Task Volunteering");
            alert.setHeaderText(selectedTask.getServiceType());
            if (selectedTask.getStatus() == 0 && (!selectedTask.getUser().getID().equals(SecondaryController.getUserLogIn().getID()))) {
                alert.setContentText(selectedTask.getServiceType() + " was picked");
                SimpleClient.getClient().sendToServer("myModify " + selectedTask.getIdNum()+ " " + SecondaryController.getUserLogIn().getID());
                alert.showAndWait();
            } else {
                Platform.runLater(() -> {
                    showCompletionMessage("Error", "You cannot volunteer to the task you asked!");
                });
            }
        }else{
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There is no tasks selected, please select one.");
            });
        }
    }

    @FXML
    void TASKDETAILS(ActionEvent event) throws IOException {
        if(selectedTask != null){
            long id= selectedTask.getIdNum();
            String serviceType= selectedTask.getServiceType();
            String fitst=selectedTask.getUser().getFirstName();
            String userid=selectedTask.getUser().getID();
            int status=selectedTask.getStatus();
            String x = String.format("Task ID: %d\nTask Description: %s\nUser Name: %s\nUser ID: %s\nState: %d", id, serviceType, fitst, userid, status);
            showAlert(x);
        }
        else{
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There is no tasks selected, please select one.");
            });
        }
    }

    @FXML
    void homepage(ActionEvent event) throws IOException {
        App.setRoot("secondary");
    }

}