
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class confirmVol {

    @FXML // fx:id="back"
    private Button back; // Value injected by FXMLLoader

    @FXML // fx:id="cancel"
    private Button confirm; // Value injected by FXMLLoader

    @FXML // fx:id="details"
    private Button details; // Value injected by FXMLLoader

    @FXML // fx:id="note"
    private TextArea note; // Value injected by FXMLLoader

    @FXML // fx:id="volList"
    private ListView<String> volList; // Value injected by FXMLLoader
    public static List<Task> getVolunteersWork = new ArrayList<>();
    private Task requestedVol = null;
    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }
    public void initialize() {
        EventBus.getDefault().register(this);
        note.setEditable(false);
        if (getVolunteersWork.isEmpty()) {
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There are no volunteer work to confirm.");
                try {
                    App.setRoot("secondary");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return;
        }
        // Add items to the ListView
        for (Task task : getVolunteersWork) {
            this.volList.getItems().add("Task id:" + task.getIdNum() + " - " + task.getServiceType());
        }
        // Set event handler for mouse click on ListView
        this.volList.setOnMouseClicked(event -> {

            String selectedTaskName = this.volList.getSelectionModel().getSelectedItem();
            if (selectedTaskName != null) {
                // Find the selected task in the getCommunityTask list
                for (Task task :getVolunteersWork) {
                    String[] parts1 = selectedTaskName.split("-");
                    String[] parts2 = parts1[0].split(":");
                    // Trim the string before parsing it as an integer
                    int number = Integer.parseInt(parts2[1].trim());
                    if (task.getIdNum() == number) {
                        requestedVol = task;
                        break;
                    }
                }
            }
        });
    }

    @Subscribe
    public void updateList(List<Task> listOfTasks) {
        System.out.println("update list123: " + listOfTasks.size());
        Platform.runLater(() -> {
            // Remove the requestedVol task if it exists
            if (requestedVol != null) {
                int indexToRemove = -1;
                for (int i = 0; i < volList.getItems().size(); i++) {
                    String listItem = volList.getItems().get(i);
                    String[] parts = listItem.split("-");
                    int taskId = Integer.parseInt(parts[0].trim().split(":")[1]);
                    if (taskId == requestedVol.getIdNum()) {
                        indexToRemove = i;
                        break;
                    }
                }
                if (indexToRemove != -1) {
                    volList.getItems().remove(indexToRemove);
                }
                // Reset requestedVol to null after removal
                requestedVol = null;
            }

            // Update the list with new tasks (excluding the removed one)
            getVolunteersWork = listOfTasks;
            for (Task task : getVolunteersWork) {
                if (task != requestedVol) { // Don't add the removed task again
                    this.volList.getItems().add("Task id:" + task.getIdNum() + " - " + task.getServiceType());
                }
            }
        });
    }


   /* @Subscribe
    public void updateList(List<Task> listOfTasks ) {
        System.out.println("update list "+ listOfTasks.size());
        Platform.runLater(() -> {
            this.volList.getItems().clear();
            getVolunteersWork = listOfTasks;
            for (Task task : getVolunteersWork) {
                this.volList.getItems().add("Task id:" + task.getIdNum() + " - " + task.getServiceType());
            }
            this.volList.setOnMouseClicked(event -> {
                String selectedTaskName = this.volList.getSelectionModel().getSelectedItem();
                if (selectedTaskName != null) {
                    // Find the selected task in the tasks list
                    for (Task task : getVolunteersWork) {
                        String[] parts1 = selectedTaskName.split("-");
                        String[] parts2 = parts1[0].split(":");
                        // Trim the string before parsing it as an integer
                        int number = Integer.parseInt(parts2[1].trim());
                        if (task.getIdNum() == number) {
                            requestedVol = task;
                            break;
                        }
                    }
                }
            });
        });
    }*/
    /*@Subscribe
    public void updateList(List<Task> listOfTasks) {
        System.out.println("update list123: " + listOfTasks.size());
        Platform.runLater(() -> {
            // Remove the requestedVol task if it exists
            if (requestedVol != null) {
                int indexToRemove = -1;
                for (int i = 0; i < volList.getItems().size(); i++) {
                    String listItem = volList.getItems().get(i);
                    String[] parts = listItem.split("-");
                    int taskId = Integer.parseInt(parts[0].trim().split(":")[1]);
                    if (taskId == requestedVol.getIdNum()) {
                        indexToRemove = i;
                        break;
                    }
                }
                if (indexToRemove != -1) {
                    volList.getItems().remove(indexToRemove);
                }
            }

            // Update the list with new tasks (excluding the removed one)
            getVolunteersWork = listOfTasks;
            for (Task task : getVolunteersWork) {
                if (task != requestedVol) { // Don't add the removed task again
                    this.volList.getItems().add("Task id:" + task.getIdNum() + " - " + task.getServiceType());
                }
            }
        });
    }*/


    @FXML
    void ConfirmVol(ActionEvent event) throws IOException {
        if(requestedVol != null){
            if (requestedVol.getStatus()==1) {
                String userid = SecondaryController.getUserLogIn().getID();
                SimpleClient.getClient().sendToServer("modTask@" + requestedVol.getIdNum() +"@" + userid);

                //long id = requestedVol.getIdNum();
                //String community = SecondaryController.getUserLogIn().getCommunity();

                //SimpleClient.getClient().sendToServer("sendMessage@" + id +"@" + userid + "@" +community);
                //String str = String.format("Task ID: %d is performed by user whose ID is: %s", id, userid);

            }try {
                showCompletionMessage("Volunteer Confirmed", "volunteer work confirmed, a message will be sent to your community manager.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else{
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There is no volunteer work selected, please select one.");
            });
        }/*
        if (requestedVol != null) {
            int id = requestedVol.getIdNum();
            User user = SecondaryController.getUserLogIn();
            System.out.println(id);
            String message = "confirm volunteer@" + id +"@"+ user.getID();
            SimpleClient.getClient().sendToServer(message);
        }*/
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showCompletionMessage(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Use INFORMATION type for completion message
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void back(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                App.setRoot("secondary");
            } catch (IOException e) {
                throw new RuntimeException(e);
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
    @FXML
    void taskDetails(ActionEvent event) {
        if(requestedVol != null){
            int id= requestedVol.getIdNum();
            String serviceType= requestedVol.getServiceType();
            String fitst=requestedVol.getUser().getFirstName();
            String userid=requestedVol.getUser().getID();
            int status=requestedVol.getStatus();
            String note= requestedVol.getNote();
            // Retrieve date and time from the requestedTask object
            LocalDate date = requestedVol.getDate();
            LocalTime time = requestedVol.getTime();
            String volId = requestedVol.getVolId();

            // Format date and time strings
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            String x = String.format("Task ID: %d\nTask Description: %s\nUser Name: %s\nUser ID: %s\nVolunteer ID: %s\nDate: %s\nTime: %s\nNote: %s",
                    id, serviceType, fitst, userid,volId, formattedDate, formattedTime, note);
            showAlert(x);
        }
        else{
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There is no volunteer work selected, please select one.");
            });
        }
    }
}
