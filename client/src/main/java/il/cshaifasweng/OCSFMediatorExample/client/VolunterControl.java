package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;

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
    public void initialize(){
        while (tasks.isEmpty()){
            try {
                Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for(Task task : tasks){
            this.TasksList.getItems().addAll(String.valueOf(task.getIdNum())+" " +task.getServiceType());
        }
        this.TasksList.setOnMouseClicked(event -> {
            String selectedTaskName = this.TasksList.getSelectionModel().getSelectedItem();


            if(selectedTaskName!=null){
                String[] parts = selectedTaskName.split("\\s+", 2);
                String firstPart = parts[0];
                for(Task task : tasks){
                    if(String.valueOf(task.getIdNum()).equals(firstPart)){
                        selectedTask = task;
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
    void PICK_TASK(ActionEvent event)throws IOException {
        Alert alert = new Alert((Alert.AlertType.INFORMATION));
        alert.setTitle("Task Volunteering");
        alert.setHeaderText(selectedTask.getServiceType());
        SimpleClient.getClient().sendToServer("modify " + selectedTask.getIdNum());
        if (selectedTask.getStatus()==0) {
            alert.setContentText(selectedTask.getServiceType() + " was picked");
        } else {
            alert.setContentText(selectedTask.getServiceType() + " is already picked");
        }
        alert.showAndWait();

    }

    @FXML
    void TASKDETAILS(ActionEvent event) throws IOException {

            if(selectedTask != null){
                int id= selectedTask.getIdNum();
                String serviceType= selectedTask.getServiceType();
                String fitst=selectedTask.getUser().getFirstName();
                String userid=selectedTask.getUser().getID();
                int status=selectedTask.getStatus();
                String x = String.format("Task ID: %d\nTask Description: %s\nUser Name: %s\nUser ID: %s\nState: %d", id, serviceType, fitst, userid, status);

                showAlert(x);
                SimpleClient.getClient().sendToServer(selectedTask);
            }


    }

    @FXML
    void homepage(ActionEvent event) throws IOException {
        App.setRoot("secondary");

    }

}
