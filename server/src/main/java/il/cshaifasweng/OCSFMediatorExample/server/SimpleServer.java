package il.cshaifasweng.OCSFMediatorExample.server;
import com.mysql.cj.xdevapi.Client;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;
import il.cshaifasweng.OCSFMediatorExample.entities.UserControl;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Parent;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class SimpleServer extends AbstractServer {
	private long key;

//	private static Session session;
//	private static SessionFactory sessionFactory = getSessionFactory();

	public SimpleServer(int port) {
		super(port);
	}
//	public static SessionFactory getSessionFactory() throws HibernateException {
//		Configuration configuration = new Configuration();
//		configuration.addAnnotatedClass(Task.class);
//		configuration.addAnnotatedClass(User.class);
//		ServiceRegistry serviceRegistry = (new StandardServiceRegistryBuilder())
//				.applySettings(configuration.getProperties()).build();
//		return configuration.buildSessionFactory(serviceRegistry);
//	}
	private static void modifyTask(int TaskID) {
		try {
			List<Task> tasks = ConnectToDataBase.getAllTasks();
			for (Task task : tasks) {
				if (task.getIdNum() == TaskID)
					task.setStatus(1);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client)  {
		try {

			String message = (String) msg;
			if (message.equals("get tasks")) {



					List<Task> alltasks = ConnectToDataBase.getAllTasks();
					client.sendToClient(alltasks);



				System.out.println("suck");

			}
			else if(message.startsWith("*")){
				Task task=new Task();
				task.setDate(LocalDate.now());
				task.setTime(LocalTime.now());
				task.setServiceType(message);
				task.setStatus(3);
				ConnectToDataBase.addTask(task);
			}
			else if(message.startsWith("modify")){
				String taskid= message.split(" ")[1];
				modifyTask(Integer.parseInt(taskid));
				client.sendToClient(ConnectToDataBase.getAllTasks());
			}

			if (message.startsWith("#LogInAttempt")) {
				try {
					handleLoginAttempt(message, client);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}


		// Add more message handling as needed
	}

	private void handleLoginAttempt(String message, ConnectionToClient client) throws Exception {
		String[] parts = message.split(",");
		if (parts.length == 2) {
			String[] credentials = parts[1].split("@");
			if (credentials.length == 2) {
				String username = credentials[0];
				String password = credentials[1];

				// Perform password validation here by querying the database
				List<UserControl> userControls = new ArrayList<>();
				List<User> allUsers = ConnectToDataBase.getAllUsers();
				for (User user : allUsers) {
					UserControl userControl = new UserControl(user.getID(), user.getFirstName(), user.getLastName(), user.getisConnected(), user.getCommentary(), user.getUsername(), "0", user.getAddress(), user.getEmail(), user.getRole());
					userControl.setSalt(user.getSalt());
					userControl.setPasswordHash(user.getPasswordHash());
					userControls.add(userControl);
				}

				boolean isValidLogin = false;
				for (UserControl user : userControls) {
					isValidLogin = user.login(username, password);
					if (isValidLogin) {


						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
							out.writeObject(user);  // assuming 'user' is your User object
							out.flush();
						} catch (IOException e) {
							// Handle serialization exception
							e.printStackTrace();
						}

						byte[] userBytes = bos.toByteArray();
						client.sendToClient(userBytes);


						// Send a success response back to the client
						try {
							if(username.startsWith("*"))
							{
								client.sendToClient("Manager_LOGIN_SUCCESS");


							}
							client.sendToClient("LOGIN_SUCCESS");

						} catch (IOException e) {
							e.printStackTrace();
						}
						return; // exit the loop since login is valid
					}
				}

				// If the loop ends and the login is not valid, send a failure response back to the client
				try {
					System.out.println("LOGIN_FAIL");
					client.sendToClient("LOGIN_FAIL");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}




//	private boolean validateLoginFromDatabase(String username, String password) {
//		List<User> users = null;
//		try {
//			users = ConnectToDataBase.getAllUsers();
//			// Proceed with handling users
//		} catch (Exception e) {
//			e.printStackTrace(); // or handle the exception appropriately
//		}
//
//		// Check if the username exists in the list of users
//		Optional<User> userOptional = users.stream()
//				.filter(user -> user.getUsername().equals(username))
//				.findFirst();
//
//		if (userOptional.isPresent()) {
//			// User with the provided username found
//			User user = userOptional.get();
//
//			// Hash the provided password with the user's salt
//			String hashedPassword = hashPassword(password, user.getSalt());
//
//			// Compare the hashed passwords
//			return hashedPassword.equals(user.getPasswordHash());
//		}
//
//		return false; // Username not found
//	}
//	String hashPassword(String password, String salt) {
//		try {
//			String passwordWithSalt = password + salt;
//			MessageDigest md = MessageDigest.getInstance("SHA-512");
//			byte[] hashedPassword = md.digest(passwordWithSalt.getBytes());
//			return bytesToHex(hashedPassword);
//		} catch (NoSuchAlgorithmException e) {
//			// Handle the exception
//			return null;
//		}
//	}
//
//	private String bytesToHex(byte[] bytes) {
//		StringBuilder result = new StringBuilder();
//		for (byte b : bytes) {
//			result.append(String.format("%02x", b));
//		}
//		return result.toString();
//	}

