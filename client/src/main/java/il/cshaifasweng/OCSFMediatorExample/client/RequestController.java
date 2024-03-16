/**
 * Sample Skeleton for 'request_control.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.UserControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class RequestController {
    @FXML // fx:id="explainService"
    private TextField explainService; // Value injected by FXMLLoader

    @FXML // fx:id="note"
    private Label note; // Value injected by FXMLLoader

    @FXML // fx:id="previousPage"
    private Button previousPage; // Value injected by FXMLLoader

    @FXML // fx:id="services"
    private ChoiceBox<String> services; // Value injected by FXMLLoader

    @FXML // fx:id="submit"
    private Button submit; // Value injected by FXMLLoader

    @FXML
    void initialize() {
        // Initialize the ChoiceBox with choices
        services.getItems().add("Dog walker");
        services.getItems().add("Buy Medicine");
        services.getItems().add("Transportation");
        services.getItems().add("Transport of furniture");
        services.getItems().add("Other");
    }

    private void showCompletionMessage(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Use INFORMATION type for completion message
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void getPrevious(ActionEvent event) throws IOException {
        App.setRoot("secondary");
    }

    private void displayTaskDetails(Task task) {
        // Display the task details to the user
        // You can show the details in a dialog, a new scene, or update a section of your current UI
        // For example, you can use an alert dialog to show the details
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Details");
        alert.setHeaderText("Task Created Successfully");
       // alert.setContentText("Service: " + task.getId() + "\nExplanation: " + task.getExplanation());
        alert.showAndWait();
    }

    @FXML
    void submitRequest(ActionEvent event) throws IOException {

        if (services.getValue() == null) {
            showAlert("Error", "Please choose one service.");
            return;
        } else if (services.getValue().equals("Other")) {
            if (explainService.getText().isEmpty()) {
                showAlert("Error", "Please fill in all required fields.");
                return; // Stop further execution
            }
        }
        try {
            showCompletionMessage("Request Submmited", "Thanks for contacting us. \nThe request details are completed successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("/********************");
        Task task = new Task();
        task.setNote(explainService.getText());
        task.setDate(LocalDate.now());
        task.setTime(LocalTime.now());
        task.setServiceType(services.getValue());
        task.setStatus(3);
       // task.setUser(UserControl.getLoggedInUser());
       // User loggedInUser = UserControl.getLoggedInUser();
       // System.out.println(UserControl.getLoggedInUser().getAddress());
       // System.out.println("******");

      /*  if (loggedInUser != null) {
            task.setUser(loggedInUser);
        } else {
            showAlert("Error", "No user logged in. Please log in before submitting a request.");
            return;
        }*/
        App.displayTaskDetails(task);
//        try {
//            SimpleClient.getClient().sendToServer(task);
//        } catch (IOException e) {
//
//            e.printStackTrace();
//        }
        SimpleClient.getClient().sendToServer("*"+services.getValue());
        App.setRoot("secondary");
    }
}
