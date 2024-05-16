package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    @FXML
    private ImageView im;
    @FXML
    private Button refresh;

    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }
    public static List<Task> requests = new ArrayList<>();
    private static Task requestedTask = null;

    public void initialize() {
        EventBus.getDefault().register(this);
        if (requests.isEmpty()) {
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There are no service requests.");
                try {
                    App.setRoot("manager_control");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return;
        }
        // Add items to the ListView
        for (Task task : requests) {
            this.communityTasks.getItems().add("Task id:" + task.getIdNum() + " - " + task.getServiceType());
        }
        // Set event handler for mouse click on ListView
        this.communityTasks.setOnMouseClicked(event -> {
            String selectedTaskName = this.communityTasks.getSelectionModel().getSelectedItem();
            if (selectedTaskName != null) {
                // Find the selected task in the getCommunityTask list
                for (Task task : requests) {
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

    @Subscribe
    public void updateList(List<Task> listOfTasks) {
        Platform.runLater(() -> {
            this.communityTasks.getItems().clear();
            requests = listOfTasks;
            if (requests.isEmpty()) {
                 /*Platform.runLater(() -> {
                    showCompletionMessage("Empty", "There are no service requests.");
                    try {
                        App.setRoot("maneger_control");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });*/
                // If requests list is empty, do nothing
                return;
            }
            for (Task task : requests) {
                this.communityTasks.getItems().addAll("Task id:" + task.getIdNum() + " - " + task.getServiceType());
            }
            this.communityTasks.setOnMouseClicked(event -> {
                String selectedTaskName = this.communityTasks.getSelectionModel().getSelectedItem();
                if (selectedTaskName != null) {
                    // Find the selected task in the tasks list
                    for (Task task : requests) {
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
        });
    }

    @FXML
    void Refresh(ActionEvent event) {
        try {
            SimpleClient.getClient().sendToServer("check requests@" + Managercontrol.getManagerLogIn().getCommunityManager());
        } catch (IOException e) {
            showAlert("Error", "Failed to get community help requests: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void showCompletionMessage(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Use INFORMATION type for completion message
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task details");
        alert.setHeaderText("Task Details: ");
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
        if (requestedTask == null)
            return;
        if (requestedTask != null) {
            int id = requestedTask.getIdNum();
            System.out.println(id);
            String message = "Task is Accept@" + (id);
            SimpleClient.getClient().sendToServer(message);
        }
        try {
            showCompletionMessage("Request accepted", "Thanks. \nThe request accepted successfully- Id request " + requestedTask.getIdNum());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
       /* try {
            SimpleClient.getClient().sendToServer("check requests@" + Managercontrol.getManagerLogIn().getCommunityManager());
        } catch (IOException e) {
            showAlert("Error", "Failed to get community help requests: " + e.getMessage());
            e.printStackTrace();
        }*/
    }


    @FXML
    void remove(ActionEvent event) throws IOException {
        if (requestedTask != null) {
            Platform.runLater(() -> {
                try {
                    App.setRoot("Reject_control");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    public static Task getRequestedTask() {
        return requestedTask;
    }

    public void setRequestedTask(Task requestedTask) {
        this.requestedTask = requestedTask;
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
        if (requestedTask != null) {
            int id = requestedTask.getIdNum();
            String serviceType = requestedTask.getServiceType();
            String fitst = requestedTask.getUser().getFirstName();
            String userid = requestedTask.getUser().getID();
            int status = requestedTask.getStatus();
            String note = requestedTask.getNote();
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
}
