package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageToUser;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import javafx.application.Platform;
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

    @FXML
    private Button refresh;

    @FXML // fx:id="memberList"
    private ListView<String> messages; // Value injected by FXMLLoader

    public static List<MessageToUser> message = new ArrayList<>();
    private MessageToUser selectedmessage = null;
    public static List<User> users = new ArrayList<>();
    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }
    @FXML
    void Refresh(ActionEvent event) {
        try {
            Object[] array = new Object[2];
            array[0] = "Get uploaded messages"; // Assign a String object to the first index
            array[1] = SecondaryController.getUserLogIn();
            SimpleClient.getClient().sendToServer(array);

        } catch (IOException e) {
            SecondaryController.showAlert("Error", "Failed to get uploaded community tasks: " + e.getMessage());
            e.printStackTrace();
        }
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

    private void showCompletionMessage(String title, String message) {
        // Display an alert dialog to the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Use INFORMATION type for completion message
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initialize() {
        if (message.isEmpty()) {
            Platform.runLater(() -> {
                showCompletionMessage("Empty", "There are no messages.");
                try {
                    App.setRoot("secondary");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
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
        if (selectedmessage != null) {
            LocalDateTime f = selectedmessage.getSentTime();
            String x = String.format("The time the message was Sent: %s", f);
            showAlert(x);
        }
    }
    private void showAlert(String task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message details");
        alert.setHeaderText("Message Details: ");
        alert.setContentText(task);
        alert.showAndWait();
    }

}
