package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.UserControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import il.cshaifasweng.OCSFMediatorExample.client.CheckRequestService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RejectControl {

    @FXML
    private Button Submit;

    @FXML
    private Button back;

    @FXML
    private ChoiceBox<String> services;


    @FXML
    void initialize() {
        // Initialize the ChoiceBox with choices
        services.getItems().add("Beyond Scope of Service or Expertise");
        services.getItems().add("Resource Constraints");
        services.getItems().add("Unrealistic Expectations or Demands");
        services.getItems().add("Violation of Policy or Law");
        services.getItems().add("Potential for Misuse or Harm");
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        App.setRoot("checkRequestService");

    }

    @FXML
    void submit(ActionEvent event) throws IOException {
        if (services.getValue() == null) {
            showAlert("Error", "Please choose one service.");
            return;

        }
        try {
            showCompletionMessage("Request canceled", "request has been rejected, a message will be sent to the user about it.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CheckRequestService service = new CheckRequestService();
        Task t = service.getRequestedTask();
        int id= t.getIdNum();
        System.out.println(id);
        String message = "Task is rejected@" + String.valueOf(id) + "@" + "4";
        SimpleClient.getClient().sendToServer(message);
        User loggedInUser = UserControl.getLoggedInUser();
        Long manger_id=loggedInUser.getkeyId();
        System.out.println(manger_id);
        Long user_id=t.getUser().getkeyId();
        SimpleClient.getClient().sendToServer("The reason of rejected is@"+"Your Request: "+service.getRequestedTask().getServiceType()+"is Rejected by your manager,the reason is: " +services.getValue()+"@"+t.getUser().getCommunity() +"@"+String.valueOf(user_id));
        System.out.println(services.getValue());




    }
    private void showAlert(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
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
}
