package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.UserControl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class distressCallControl {
    @FXML
    private TextField codetx;
    @FXML
    private Button Ambulancebtn;

    @FXML
    private Button FireBtn;


    @FXML
   private Button policebtn;




    @FXML
    void codetxt(ActionEvent event) {
//        User loggedInUser = UserControl.getLoggedInUser();
//        String x=codetx.getText();
//        int number = Integer.parseInt(x);
//        System.out.println(number);
//        if(loggedInUser.getkeyId()==number)
//        {
//            Ambulancebtn.setDisable(true);
//            FireBtn.setDisable(true);
//            policebtn.setDisable(true);
//
//        }


    }

    @FXML
    void saveCall(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String x = codetx.getText();

        // Check if the TextField is empty
        //if (clickedButton == Ambulancebtn) {
            if (x.isEmpty())
            {
                System.out.println("Wrong");
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                    alert.setTitle("Request Error"); // Set the window's title
                    alert.setHeaderText(null); // Optional: you can have a header or set it to null
                    alert.setContentText("your Request is denied,please enter your code"); // Set the main message/content
                    alert.showAndWait(); // Display the alert and wait for the user to close it
                });

            }
            else {
                try {
                    // Attempt to convert the text to an integer
                    int number = Integer.parseInt(x);
                    // Conversion was successful
                    System.out.println("Number entered: " + number);
                    SimpleClient.getClient().sendToServer("The key id is:"+x);


                    // Proceed with handling the integer input
                } catch (NumberFormatException e) {
                    // Conversion to integer failed
                    System.out.println("Invalid input: not an integer");
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                        alert.setTitle("Request Error"); // Set the window's title
                        alert.setHeaderText(null); // Optional: you can have a header or set it to null
                        alert.setContentText("your Request is denied,please enter integer code"); // Set the main message/content
                        alert.showAndWait(); // Display the alert and wait for the user to close it
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

       // }


    }



}
