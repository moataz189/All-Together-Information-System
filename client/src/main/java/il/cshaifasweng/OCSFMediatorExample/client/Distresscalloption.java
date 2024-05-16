package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.UserControl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Distresscalloption {
    @FXML
    private TextField id;

    @FXML
    private TextField name;
    private static User managerLogIn;
    @FXML
    private ChoiceBox<String> Time;
    @FXML
    private DatePicker date;
    @FXML // fx:id="im"
    private ImageView im; // Value injected by FXMLLoader

    public static LocalDate selectedDate;
    public static int type_calls = 0;
    @FXML
    private Button DistressButtonControl;

    @FXML
    void distress(ActionEvent event) throws IOException {
        App.setRoot("distressCallsecondary");
    }
    public void initialize() {
        ArrayList<User> loggedInList = UserControl.getLoggedInList();
        if (!loggedInList.isEmpty()) {
            User lastUser = loggedInList.get(loggedInList.size() - 1);
            name.setText(lastUser.getFirstName() + " " + lastUser.getLastName());
            id.setText(lastUser.getID());
            managerLogIn = lastUser;
        }
        id.setEditable(false);
        name.setEditable(false);
    }

    @FXML
    void date(ActionEvent event) {
        System.out.println(date.getValue());
    }

    public static User getManagerLogIn() {
        return managerLogIn;
    }

    // Setter for userLogIn
    public static void setUserLogIn(User user) {
        managerLogIn = user;
    }

    @FXML
    void Back(ActionEvent event) throws IOException {
        App.setRoot("manager_control");
    }

    @FXML
    void All(ActionEvent event) {
        type_calls = 1;
        if (date.getValue() == null) {
            //SimpleClient.getClient().sendToServer("is not Registered");
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                alert.setTitle("Request Error"); // Set the window's title
                alert.setHeaderText(null); // Optional: you can have a header or set it to null
                alert.setContentText("Your Request is denied, please select a legal date."); // Set the main message/content
                alert.showAndWait(); // Display the alert and wait for the user to close it
            });
        } else try {
            LocalDate currentDate = LocalDate.now();
            if (date.getValue().isAfter(currentDate)) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                    alert.setTitle("Request Error"); // Set the window's title
                    alert.setHeaderText(null); // Optional: you can have a header or set it to null
                    alert.setContentText("Your Request is denied, please select a legal date"); // Set the main message/content
                    alert.showAndWait(); // Display the alert and wait for the user to close it
                });
            } else {
                selectedDate = date.getValue();
                SimpleClient.getClient().sendToServer("All communities@" + date.getValue());
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to get communities Distress calls: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void YOUR_Community(ActionEvent event) {
        type_calls = 0;
        if (date.getValue() == null) {
            //SimpleClient.getClient().sendToServer("is not Registered");

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                alert.setTitle("Request Error"); // Set the window's title
                alert.setHeaderText(null); // Optional: you can have a header or set it to null
                alert.setContentText("Your Request is denied,please select date"); // Set the main message/content
                alert.showAndWait(); // Display the alert and wait for the user to close it
            });
        } else try {
            LocalDate currentDate = LocalDate.now();
            if (date.getValue().isAfter(currentDate)) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                    alert.setTitle("Request Error"); // Set the window's title
                    alert.setHeaderText(null); // Optional: you can have a header or set it to null
                    alert.setContentText("Your Request is denied,please select a correct date"); // Set the main message/content
                    alert.showAndWait(); // Display the alert and wait for the user to close it
                });
            } else {
                selectedDate = date.getValue();
                SimpleClient.getClient().sendToServer("My community@" + managerLogIn.getCommunityManager() + "@" + date.getValue());

            }

        } catch (IOException e) {
            showAlert("Error", "Failed to get community Distress calls: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void histogram(ActionEvent event) throws IOException {
        type_calls = 1;
        if (date.getValue() == null) {
            //SimpleClient.getClient().sendToServer("is not Registered");
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                alert.setTitle("Request Error"); // Set the window's title
                alert.setHeaderText(null); // Optional: you can have a header or set it to null
                alert.setContentText("your Request is denied,please select date"); // Set the main message/content
                alert.showAndWait(); // Display the alert and wait for the user to close it
            });
        } else try {
            LocalDate currentDate = LocalDate.now();
            if (date.getValue().isAfter(currentDate)) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                    alert.setTitle("Request Error"); // Set the window's title
                    alert.setHeaderText(null); // Optional: you can have a header or set it to null
                    alert.setContentText("your Request is denied,please select a correct date"); // Set the main message/content
                    alert.showAndWait(); // Display the alert and wait for the user to close it
                });


            } else {
                selectedDate = date.getValue();
                SimpleClient.getClient().sendToServer("11get all calls@" + date.getValue());
            }

        } catch (IOException e) {
            showAlert("Error", "Failed to get community Distress calls: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void community_histogram(ActionEvent event) {
        type_calls = 0;
        if (date.getValue() == null) {
            //SimpleClient.getClient().sendToServer("is not Registered");
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                alert.setTitle("Request Error"); // Set the window's title
                alert.setHeaderText(null); // Optional: you can have a header or set it to null
                alert.setContentText("your Request is denied,please select date"); // Set the main message/content
                alert.showAndWait(); // Display the alert and wait for the user to close it
            });
        } else try {
            LocalDate currentDate = LocalDate.now();
            if (date.getValue().isAfter(currentDate)) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                    alert.setTitle("Request Error"); // Set the window's title
                    alert.setHeaderText(null); // Optional: you can have a header or set it to null
                    alert.setContentText("your Request is denied,please select a correct date"); // Set the main message/content
                    alert.showAndWait(); // Display the alert and wait for the user to close it
                });
            } else {
                selectedDate = date.getValue();
                SimpleClient.getClient().sendToServer("11get my calls@" + managerLogIn.getCommunityManager() + "@" + date.getValue());

            }
        } catch (IOException e) {
            showAlert("Error", "Failed to get community Distress calls: " + e.getMessage());
            e.printStackTrace();
        }
    }
}