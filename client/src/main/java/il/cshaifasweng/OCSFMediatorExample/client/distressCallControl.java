package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class distressCallControl {
    @FXML
    private TextField codetx;
    @FXML
    private Button Ambulancebtn;

    @FXML
    private Button FireBtn;
    @FXML
    private ImageView im1;

    @FXML
    private ImageView im2;

    @FXML
    private ImageView im3;

    @FXML
    private Button policebtn;
    @FXML
    private ChoiceBox<String> Location;
    @FXML
    private Button Back;

    @FXML
    void initialize() {
        // Initialize the ChoiceBox with choices
        Location.getItems().add("Yaffa Nazareth");
        Location.getItems().add("Kabul");
        Location.getItems().add("Nazareth");
        Location.getItems().add("Tamra");
        Location.getItems().add("Kukab");
        Location.getItems().add("UMM El Fahem");
    }

    @FXML
    void codetxt(ActionEvent event) {
    }


    @FXML
    void saveCall(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        String x = codetx.getText();
        String buttonText = clickedButton.getText();
        if (x.isEmpty()) {
            SimpleClient.getClient().sendToServer("is not Registered");
            System.out.println("Wrong");
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                alert.setTitle("Request Error"); // Set the window's title
                alert.setHeaderText(null); // Optional: you can have a header or set it to null
                alert.setContentText("Your Request is denied,please enter your code."); // Set the main message/content
                alert.showAndWait(); // Display the alert and wait for the user to close it
            });

        } else if (Location.getValue() == null) {
            //SimpleClient.getClient().sendToServer("is not Registered");
            System.out.println("Wrong");
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                alert.setTitle("Request Error"); // Set the window's title
                alert.setHeaderText(null); // Optional: you can have a header or set it to null
                alert.setContentText("Your Request is denied,please select your Location"); // Set the main message/content
                alert.showAndWait(); // Display the alert and wait for the user to close it
            });
        } else {
            try {
                // Attempt to convert the text to an integer
                int number = Integer.parseInt(x);
                // Conversion was successful
                System.out.println("Number entered: " + number);
                System.out.println(buttonText);
                SimpleClient.getClient().sendToServer("The key id is:" + x + "@" + buttonText + "@" + Location.getValue());

                // Proceed with handling the integer input
            } catch (NumberFormatException e) {
                SimpleClient.getClient().sendToServer("is not Registered");
                // Conversion to integer failed
                System.out.println("Invalid input: not an integer");
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                    alert.setTitle("Request Error"); // Set the window's title
                    alert.setHeaderText(null); // Optional: you can have a header or set it to null
                    alert.setContentText("Your Request is denied,please enter integer code"); // Set the main message/content
                    alert.showAndWait(); // Display the alert and wait for the user to close it
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void Back(ActionEvent event) throws IOException {
        App.setRoot("primary");
    }
}