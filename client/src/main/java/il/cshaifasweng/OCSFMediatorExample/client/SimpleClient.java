package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.scene.control.Alert;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

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


    protected void handleMessageFromServer(Object msg) throws IOException {
        try {
            if (msg instanceof Object[]) {
                System.out.println("check the if");
                Object[] messageParts = (Object[]) msg;
                if (messageParts.length == 2 && messageParts[0] instanceof String && messageParts[1] instanceof List) {
                    if (messageParts[0].equals("request")) {
                        System.out.println("adhhhh");
                        CheckRequestService.requests = (List<Task>) messageParts[1];

                        try {
                            App.setRoot("checkRequestService");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (messageParts[0].equals("alltasks")) {
                        VolunterControl.tasks = (List<Task>) messageParts[1];
                        App.setRoot("volunter_control");
                    } else if (messageParts[0].equals("uploaded")) {
                        CommunityTaskControl.getCommunityTask = (List<Task>) messageParts[1];
                        Platform.runLater(() -> {
                            try {
                                App.setRoot("CommunityTasks");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    else if (messageParts[0].equals("Messages")) {
                        System.out.println("hh");
                        MessagesToUser.message= (List<MessageToUser>) messageParts[1];
                       // MessagesToUser.users=(List<User>)messageParts[2];
                        Platform.runLater(() -> {
                            try {
                                App.setRoot("MessagesToUser");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    else if (messageParts[0].equals("all users send")) {


                         MessagesToUser.users=(List<User>)messageParts[1];

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
        try {
            System.out.println("one parametr");

            if (msg instanceof String) {
                String message = (String) msg;
                // Handling LOGIN_FAIL message
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
                    } else {
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
                    if (firstElement instanceof User) {
                        System.out.println("User members received");
                        CommunityMembers.users = (List<User>) msg;
                        App.setRoot("communityMembers");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SimpleClient getClient() {
        if (client == null) {
            client = new SimpleClient("localhost", 3000);
        }
        return client;
    }
}