/**
 * Sample Skeleton for 'newTaskData.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;


public class NewTaskDataControl {

    @FXML // fx:id="back"
    private Button back; // Value injected by FXMLLoader

    @FXML // fx:id="newData"
    private TextField newData; // Value injected by FXMLLoader

    @FXML // fx:id="save"
    private Button save; // Value injected by FXMLLoader

    @FXML // fx:id="taskId"
    private TextField taskId; // Value injected by FXMLLoader
    @FXML
    private TextField title;

    @FXML // fx:id="note"
    private TextArea note; // Value injected by FXMLLoader
    @FXML
    private ImageView im1;

    @FXML
    private ImageView im2;

    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }
    public void initialize() {
        title.setEditable(false);
        note.setEditable(false);
        title.setText("Please enter the value of "+ UpdateTaskDetails.getUpdateVale()+".");
    }
    @FXML
    void homePage(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                App.setRoot("updateTaskDetails");
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
    void saveData(ActionEvent event) {
        // Check if the new data and task ID fields are empty
        if (newData.getText().isEmpty() || taskId.getText().isEmpty()) {
            showAlert("Error", "Please fill in all required fields.");
            return;
        }
       // if(UpdateTaskDetails.updateVale.equals("status"))
        if (!taskId.getText().matches("[0-9]+")) {
            showAlert("Error", "Task id should contain only numbers.");
            taskId.clear();
            newData.clear();
            return;
        }

        // Check if the new data contains only digits and/or semicolons
        if (!newData.getText().matches("[0-9.]+")) {
            showAlert("Error", "New data should contain only numbers and/or semicolons.");
            taskId.clear();
            newData.clear();
            return;
        }
        try {
            // Construct the message to send to the server
            String message = "update data@" + taskId.getText() + "@" + newData.getText() + "@" + UpdateTaskDetails.getUpdateVale()+"@"+
                    Managercontrol.getManagerLogIn().getCommunityManager();
            // Send the message to the server
            SimpleClient.getClient().sendToServer(message);

        } catch (IOException e) {
            // Show an error alert if failed to update task status
            showAlert("Error", "Failed to update task status: " + e.getMessage());
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
}


