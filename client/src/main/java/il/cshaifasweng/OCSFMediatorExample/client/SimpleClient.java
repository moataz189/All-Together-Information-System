package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.UserControl;
import javafx.scene.control.Alert;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class SimpleClient extends AbstractClient {

    private static SimpleClient client = null;

    private SimpleClient(String host, int port) {
        super(host, port);
    }


    public void handleMessageFromServer(Object msg) throws IOException {
        try {
            if (msg instanceof Object[]) {
                System.out.println("check the if");
                Object[] messageParts = (Object[]) msg;
                if (messageParts.length == 2 && messageParts[0] instanceof String && messageParts[1] instanceof List) {
                    if (messageParts[0].equals("request")) {
                        System.out.println("request");
                        CommunityTaskControl.getCommunityTask = (List<Task>) messageParts[1];
                        App.setRoot("CommunityTasks");
                    }
                    if (messageParts[0].equals("alltasks")) {
                        VolunterControl.tasks = (List<Task>) messageParts[1];
                        App.setRoot("volunter_control");
                    }
                    if (messageParts[0].equals("done")) {
                        VolunterControl.tasks = (List<Task>) messageParts[1];
                        App.setRoot("CommunityTasks");
                    }
                }
            } else {
                handleMessageFromServer1(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void handleMessageFromServer1(Object msg) throws IOException {
        System.out.println("one parametr");

        if (msg instanceof String) {
            String message = (String) msg;
            // Handling LOGIN_FAIL message
            if("The key id is true".equals(message))
            {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION); // Use ERROR alert type
                    alert.setTitle("Request transferred"); // Set the window's title
                    alert.setHeaderText(null); // Optional: you can have a header or set it to null
                    alert.setContentText("your Request has been transferred to the required service center"); // Set the main message/content
                    alert.showAndWait(); // Display the alert and wait for the user to close it
                });


//                distressCallControl instance = new distressCallControl(); // You need to have a way to get the actual instance of distressCallControl
//                instance.enablePoliceButton(true);
            }
            if("The key id is false".equals(message)) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                    alert.setTitle("Request Error"); // Set the window's title
                    alert.setHeaderText(null); // Optional: you can have a header or set it to null
                    alert.setContentText("Pleas enter your correct code"); // Set the main message/content
                    alert.showAndWait(); // Display the alert and wait for the user to close it
                });
            }
            if ("LOGIN_FAIL".equals(message)) {
                System.out.println("Login failed. Please try again.");

                // Ensure this runs on the JavaFX Application Thread
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR); // Use ERROR alert type
                    alert.setTitle("Login Error"); // Set the window's title
                    alert.setHeaderText(null); // Optional: you can have a header or set it to null
                    alert.setContentText("Login failed. Please try again."); // Set the main message/content
                    alert.showAndWait(); // Display the alert and wait for the user to close it
                });
            } else {
                if ("LOGIN_SUCCESS".equals(message)) {
                    App.setRoot("secondary");
                } else if("Manager_LOGIN_SUCCESS".equals(message)) {
                    App.setRoot("manager_control");
                }

            }
        }
        if (msg.getClass().equals(Warning.class)) {
            EventBus.getDefault().post(new WarningEvent((Warning) msg));
        }
        if (msg instanceof List) {
            List<?> list = (List<?>) msg; // Cast to a list of unknown type
            if (!list.isEmpty()) {
                Object firstElement = list.get(0);
               /* if (firstElement instanceof Task) {
                    System.out.println("tasks received");
                    //VolunterControl.tasks = (List<Task>) msg;
                   // App.setRoot("volunter_control");
                }*/
                if (firstElement instanceof User) {
                    System.out.println("User members received");
                    CommunityMembers.users = (List<User>) msg;
                    App.setRoot("communityMembers");
                    // Proceed with setting the root or any other necessary operations
                }
            }
        }
        if (msg instanceof byte[]) {
            byte[] receivedUserBytes = (byte[]) msg;

            try (ByteArrayInputStream bis = new ByteArrayInputStream(receivedUserBytes);
                 ObjectInputStream in = new ObjectInputStream(bis)) {

                // Deserialize the User object received from the server
                User user = (User) in.readObject();
                UserControl.setLoggedInUser(user);
                UserControl.setLoggedInUser(user);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static SimpleClient getClient() {
        if (client == null) {
            client = new SimpleClient("localhost", 3000);
        }
        return client;
    }
}