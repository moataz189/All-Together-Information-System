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

	@Override
	protected void handleMessageFromServer(Object msg) throws IOException {
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
			}



			else{
				if("LOGIN_SUCCESS".equals(message))
				{
					App.setRoot("secondary");
				}
				else {
					App.setRoot("manager_control");
				}

			}


		}
		if(msg.getClass().equals(Warning.class))
		{
			EventBus.getDefault().post(new WarningEvent((Warning)msg));
		}
		if (msg instanceof List)
		{

			VolunterControl.tasks = (List<Task>) msg;

			App.setRoot("volunter_control");
		}
		if (msg instanceof byte[]) {
			byte[] receivedUserBytes = (byte[]) msg;

			try (ByteArrayInputStream bis = new ByteArrayInputStream(receivedUserBytes);
				 ObjectInputStream in = new ObjectInputStream(bis)) {

				// Deserialize the User object received from the server
				User user = (User) in.readObject();
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
