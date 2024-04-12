package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageToUser;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessagesToUser {

    @FXML
    private Button back;

    @FXML
    private Button details;
    @FXML // fx:id="memberList"
    private ListView<String> messages; // Value injected by FXMLLoader

    public static List<MessageToUser> message = new ArrayList<>();
    private MessageToUser selectedmessage = null;
    public static List<User> users = new ArrayList<>();

    @FXML
    void back(ActionEvent event) throws IOException {
        App.setRoot("secondary");

    }
    public void initialize() {
        if (message.isEmpty()) {
            return;
        }

        while (message.isEmpty()) {
            try {
                Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (MessageToUser msg : message) {
            this.messages.getItems().add(String.valueOf(msg.getContent()));
        }

        this.messages.setOnMouseClicked(event -> {
            String selectedMsgId = this.messages.getSelectionModel().getSelectedItem();
            if (selectedMsgId != null) {
                for (MessageToUser msg : message) {
                    if (String.valueOf(msg.getContent()).equals(selectedMsgId)) {
                        selectedmessage = msg;
                        break;
                    }
                }
            }
        });
    }


    @FXML
    void details(ActionEvent event) {
        if(selectedmessage!=null)
        {
            String Manager="none";
            String email = "none";
            for(User user:users)
            {

                if(user.getkeyId().equals(selectedmessage.getSender()))
                    {
                        Manager= user.getFirstName()+" "+user.getLastName();
                        email=user.getEmail();

                }
            }
            LocalDateTime f=selectedmessage.getSentTime();
            String x = String.format(" Manager Name:  %s\nManager Email: %s\n The time the message was Sent: %s",  Manager, email, f);
            showAlert(x);
        }


    }
    private void showAlert(String task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message details");
        alert.setHeaderText("Manager and message Details: ");
        alert.setContentText(task);
        alert.showAndWait();
    }

}