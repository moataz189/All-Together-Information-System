
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class displayTaskController {
    @FXML // fx:id="close"
    private Button close; // Value injected by FXMLLoader
    @FXML // fx:id="date"
    private TextField date; // Value injected by FXMLLoader

    @FXML // fx:id="idTask"
    private TextField idTask; // Value injected by FXMLLoader

    @FXML // fx:id="note"
    private TextField note; // Value injected by FXMLLoader

    @FXML // fx:id="phone"
    private TextField email; // Value injected by FXMLLoader

    @FXML // fx:id="service"
    private TextField service; // Value injected by FXMLLoader

    @FXML // fx:id="submmiter"
    private TextField submmiter; // Value injected by FXMLLoader

    @FXML // fx:id="time"
    private TextField time; // Value injected by FXMLLoader
    @FXML
    void closePage(ActionEvent event) throws IOException {
        Stage stage = (Stage) close.getScene().getWindow();
        // Close the stage
        stage.close();
    }

    public void initData(Task task) {
        // Update UI elements with task details

        date.setText(String.valueOf(task.getDate()));
        time.setText(String.valueOf(task.getTime()));
        service.setText(task.getServiceType());
        email.setText(task.getUser().getEmail());
        User user = SecondaryController.getUserLogIn();
        submmiter.setText(user.getFirstName()+ " "+user.getLastName());
        note.setText(task.getNote());
    }
}
