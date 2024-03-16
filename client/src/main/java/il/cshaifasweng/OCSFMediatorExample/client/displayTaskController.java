/**
 * Sample Skeleton for 'displayTaskDetails.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;
import com.google.protobuf.StringValue;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.UserControl;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class displayTaskController {

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

    public void initData(Task task) {
        // Update UI elements with task details
        idTask.setText(String.valueOf(task.getIdNum()));
        date.setText(String.valueOf(task.getDate()));
        time.setText(String.valueOf(task.getTime()));
        service.setText(task.getServiceType());
        //email.setText(task.getUser().getEmail());
        User user = UserControl.getLoggedInUser();
        submmiter.setText(user.getFirstName()+ " "+user.getLastName());
        note.setText(task.getNote());
    }
}
