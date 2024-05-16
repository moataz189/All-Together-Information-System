
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
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
    private ImageView image;
    private InputStream stream;

    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }
    @FXML
    void initialize() {

        // Initialize the ChoiceBox with choices
        services.getItems().add("Dog walker");
        services.getItems().add("Buy Medicine");
        services.getItems().add("Transportation");
        services.getItems().add("Transport of furniture");
        services.getItems().add("Home cleaning");
        services.getItems().add("House keeping");
        services.getItems().add("Personal Chef");
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

    @FXML
    private void submitRequest(ActionEvent event) throws IOException {

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
            showCompletionMessage("Request Submmited", "Thanks for contacting us. \nThe request details have been completed successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Task task = new Task();
        task.setNote(explainService.getText());
        task.setDate(LocalDate.now());
        task.setTime(LocalTime.now());
        task.setServiceType(services.getValue());
        task.setStatus(3);
        task.setUser(SecondaryController.getUserLogIn());
        App.displayTaskDetails(task);
        try {
            Object[] array = new Object[2];
            array[0] = "add task to database."; // Assign a String object to the first index
            array[1] = task;
            SimpleClient.getClient().sendToServer(array);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            try {
                App.setRoot("secondary");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
