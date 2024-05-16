

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class UpdateTaskDetails {

    @FXML // fx:id="back"
    private Button back; // Value injected by FXMLLoader

    @FXML // fx:id="status"
    private Button status; // Value injected by FXMLLoader
    @FXML
    private TextArea text;
    @FXML // fx:id="time"
    private Button time; // Value injected by FXMLLoader

    @FXML
    private ImageView im;
    private static String updateVale;

    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }
    public static String getUpdateVale() {
        return updateVale;
    }
    public void initialize() {
        text.setEditable(false);

    }

    public static void setUpdateVale(String updateVale) {
        UpdateTaskDetails.updateVale = updateVale;
    }

    @FXML
    void UpdateExecutionTime(ActionEvent event) {
        try {
            setUpdateVale("execution time");
            Platform.runLater(() -> {
                try {

                    App.setRoot("newTaskData");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            SimpleClient.getClient().sendToServer(updateVale);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void homePage(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                App.setRoot("manager_control");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void updateStatus(ActionEvent event) {
        try {
            setUpdateVale("status");
            SimpleClient.getClient().sendToServer(updateVale);
            Platform.runLater(() -> {
                try {

                    App.setRoot("newTaskData");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
