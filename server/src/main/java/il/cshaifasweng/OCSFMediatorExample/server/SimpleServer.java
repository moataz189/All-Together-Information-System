package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.client.UpdateTaskDetails;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class SimpleServer extends AbstractServer {
    private long key;
    public SimpleServer(int port) {
        super(port);
    }

    private static void modifyTask(int TaskID) {
        try {
            List<Task> tasks = ConnectToDataBase.getAllTasks();
            if (tasks != null) {
                for (Task task : tasks) {
                    if (task.getIdNum() == TaskID) {
                        task.setStatus(1);
                        // Optionally, break the loop if you found the task
                        break;
                    }
                }
            } else {
                System.out.println("Tasks list is null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void modifyCompltedTask(int TaskID) {
        try {
            List<Task> tasks = ConnectToDataBase.getAllTasks();
            for (Task task : tasks) {
                if (task.getIdNum() == TaskID) {
                    task.setStatus(2);
                    ConnectToDataBase.updateTaskData("2", task, "status");
                    // Delete reminder messages associated with this task ID
                    ConnectToDataBase.deleteMessagesByTaskId(String.valueOf(TaskID));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void myModifyTask(int TaskID, String volunteerId) {
        try {
            List<Task> tasks = ConnectToDataBase.getAllTasks();
            String TaskIDString = String.valueOf(TaskID);
            for (Task task : tasks) {
                if (task.getIdNum() == TaskID) {
                    task.setStatus(1);
                    ConnectToDataBase.updateTaskData("1", task, "status");

                    // Schedule reminder for this task
                    TaskScheduler.scheduleTaskReminder(TaskID, volunteerId);

                    ConnectToDataBase.deleteMessagesByTaskId(TaskIDString);
                    task.setVolId(volunteerId);
                    ConnectToDataBase.updateTaskVolunteerId(volunteerId, task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override

    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        try {
            if (msg instanceof Object[]) {
                Object[] messageParts = (Object[]) msg;
                if (messageParts.length == 2 && messageParts[0] instanceof String && messageParts[0].equals("add task to database.")
                        && messageParts[1] instanceof Task) {
                    ConnectToDataBase.addTask((Task) messageParts[1]);
                    sendToAllClients((Task) messageParts[1]);// to update the requests that the client can cancel.
                    Message update = new Message("update uploaded tasks list");
                    update.setObj((Task) messageParts[1]);
                    sendToAllClients(update);// to update the requests that the client can cancel.

                } else if (messageParts.length == 2 && messageParts[0] instanceof String &&
                        messageParts[0].equals("Cancel request") && messageParts[1] instanceof User) {
                    List<Task> requests = ConnectToDataBase.getTasksWithStatusAndUser(((User) messageParts[1]).getID());
                    Object[] array = new Object[2];
                    array[0] = "ToCancel"; // Assign a String object to the first index
                    array[1] = requests;
                    client.sendToClient(array);
                } else if (messageParts.length == 2 && messageParts[0] instanceof String &&
                        messageParts[0].equals("Confirm Volunteer") && messageParts[1] instanceof User) {
                    List<Task> volunteers = ConnectToDataBase.getTasksWithVolId(((User) messageParts[1]).getID());
                    List<Task> newOne = new ArrayList<>();
                    for (Task task : volunteers) {
                        if (task.getStatus() == 1) {
                            newOne.add(task);
                        }
                    }
                    Object[] array = new Object[2];
                    array[0] = "ToConfirm"; // Assign a String object to the first index
                    array[1] = newOne;
                    client.sendToClient(array);
                } else if (messageParts.length == 2 && messageParts[0] instanceof String &&
                        messageParts[0].equals("Get uploaded messages") && messageParts[1] instanceof User) {
                    List<MessageToUser> requests = ConnectToDataBase.getMessagesBySender(((User) messageParts[1]).getID());
                    Object[] array = new Object[2];
                    array[0] = "Messages"; // Assign a String object to the first index
                    array[1] = requests;
                    client.sendToClient(array);
                } else if (messageParts.length == 2 && messageParts[0] instanceof String &&
                        messageParts[0].equals("Get maneger messages")) {
                    //List<MessageToManeger> requests = ConnectToDataBase.getMessagesByRecipient(String.valueOf(messageParts[1]));
                    Object[] array = new Object[2];
                    array[0] = "ManMessages"; // Assign a String object to the first index
                    //array[1] = requests;
                    client.sendToClient(array);
                }
                return;
            }
            String message = (String) msg;
            if (message.startsWith("My community")) {
                String[] parts1 = message.split("@");
                List<DistressCall> calls = ConnectToDataBase.getDistressCallsBetweenDates(parts1[1], LocalDate.parse(parts1[2]));
                Object[] array = new Object[2];
                array[0] = "my community calls"; // Assign a String object to the first index
                array[1] = calls;
                client.sendToClient(array);

            }
            if (message.startsWith("11get my calls")) {
                String[] parts1 = message.split("@");
                List<DistressCall> calls = ConnectToDataBase.getDistressCallsBetweenDates(parts1[1], LocalDate.parse(parts1[2]));
                Object[] array = new Object[2];
                array[0] = "histograms calls"; // Assign a String object to the first index
                array[1] = calls;
                client.sendToClient(array);
            }
            if (message.startsWith("11get all calls")) {
                String[] parts1 = message.split("@");
                List<DistressCall> calls = ConnectToDataBase.getallDistressCallsBetweenDates(LocalDate.parse(parts1[1]));
                Object[] array = new Object[2];
                array[0] = "histograms calls"; // Assign a String object to the first index
                array[1] = calls;
                client.sendToClient(array);

            } else if (message.startsWith("All communities@")) {
                String[] parts1 = message.split("@");
                // Extract the target date from the message
                LocalDate targetDate = LocalDate.parse(parts1[1]);

                // Retrieve distress calls between the target date
                List<DistressCall> calls = ConnectToDataBase.getallDistressCallsBetweenDates(targetDate);
                System.out.println("all communities" + targetDate + "size" + calls.size());

                // Prepare data to send to the client
                Object[] array = new Object[2];
                array[0] = "all communities calls"; // Assign a String object to the first index
                array[1] = calls;
                // Send data back to the client
                client.sendToClient(array);
            }
            if (message.startsWith("The key")) {
                String[] parts1 = message.split("@");
                String[] parts = parts1[0].split(":", 2);
                if (parts.length < 2) {

                }
                String keyStr = parts[1].trim();

                if (keyStr.isEmpty()) {
                    System.out.println("There is no key id provided.");
                } else {
                    // Attempt to convert the trimmed string to an int
                    try {
                        int keyId = Integer.parseInt(keyStr);
                        System.out.println("The key id is a number: " + keyId);
                        List<User> allUsers = ConnectToDataBase.getusersList();
                        boolean x = false;
                        for (User user : allUsers) {
                            if (user.getkeyId() == keyId) {
                                client.sendToClient("The key id is true");
                                String user_id = user.getID();
                                x = true;
                                List<EmergencyCenter> allcenters = ConnectToDataBase.getAllcenters();
                                for (EmergencyCenter emergencyCenter : allcenters) {
                                    if (emergencyCenter.getLocation().equals(parts1[2])) {
                                        if (emergencyCenter.getService().equals(parts1[1])) {
                                            DistressCall newdistress = new DistressCall();
                                            newdistress.setRegistered(true);
                                            newdistress.setUser(user);
                                            newdistress.setDate(LocalDate.now());
                                            newdistress.setLocation(parts1[2]);
                                            newdistress.setTime(LocalTime.now());
                                            newdistress.setUser_ID(user_id);
                                            newdistress.setEmergencyCenter(emergencyCenter);
                                            ConnectToDataBase.Add_distress(newdistress);
                                            Message update = new Message("update manager distress call list");
                                            update.setObj(newdistress);
                                            sendToAllClients(update);// to update the requests that the client can cancel.
                                            update = new Message("update manager distress call histogram");
                                            update.setObj(newdistress);
                                            sendToAllClients(update);
                                        }
                                    }
                                }
                            }
                        }
                        if (x == false) {
                            List<EmergencyCenter> allcenters = ConnectToDataBase.getAllcenters();
                            for (EmergencyCenter emergencyCenter : allcenters) {
                                if (emergencyCenter.getLocation().equals(parts1[2])) {
                                    if (emergencyCenter.getService().equals(parts1[1])) {
                                        DistressCall newdistress = new DistressCall();
                                        newdistress.setRegistered(false);
                                        //newdistress.setUser(user);
                                        newdistress.setDate(LocalDate.now());
                                        newdistress.setLocation(parts1[2]);
                                        newdistress.setTime(LocalTime.now());
                                        //newdistress.setUser_ID(user_id);
                                        newdistress.setEmergencyCenter(emergencyCenter);
                                        ConnectToDataBase.Add_distress(newdistress);
                                        Message update = new Message("update manager distress call list");
                                        update.setObj(newdistress);
                                        client.sendToClient("The key id is false");
                                        sendToAllClients(update);// to update the requests that the client can cancel.
                                        update = new Message("update manager distress call histogram");
                                        update.setObj(newdistress);
                                        sendToAllClients(update);
                                    }
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("The provided key id is not a valid number.");
                    }
                }
            }
            if (message.startsWith("2The key")) {
                String[] parts1 = message.split("@");
                String[] parts = parts1[0].split(":", 2);
                if (parts.length < 2) {

                }
                String keyStr = parts[1].trim();

                if (keyStr.isEmpty()) {
                    System.out.println("There is no key id provided.");
                } else {
                    // Attempt to convert the trimmed string to an int
                    try {
                        List<User> allUsers = ConnectToDataBase.getusersList();
                        boolean x = false;
                        for (User user : allUsers) {
                            if (user.getID().equals(keyStr)) {
                                client.sendToClient("distresscall added successfully to the database.");
                                String user_id = user.getID();
                                x = true;
                                List<EmergencyCenter> allcenters = ConnectToDataBase.getAllcenters();
                                for (EmergencyCenter emergencyCenter : allcenters) {
                                    if (emergencyCenter.getLocation().equals(parts1[2])) {
                                        if (emergencyCenter.getService().equals(parts1[1])) {
                                            DistressCall newdistress = new DistressCall();
                                            newdistress.setRegistered(true);
                                            newdistress.setDate(LocalDate.now());
                                            newdistress.setLocation(parts1[2]);
                                            newdistress.setTime(LocalTime.now());
                                            newdistress.setUser_ID(user_id);
                                            newdistress.setEmergencyCenter(emergencyCenter);
                                            newdistress.setUser(user);
                                            ConnectToDataBase.Add_distress(newdistress);
                                            Message update = new Message("update manager distress call list");
                                            update.setObj(newdistress);
                                            sendToAllClients(update);// to update the requests that the client can cancel.
                                            update = new Message("update manager distress call histogram");
                                            update.setObj(newdistress);
                                            sendToAllClients(update);
                                        }
                                    }
                                }
                            }
                        }
                        if (x == false) {
                            List<EmergencyCenter> allcenters = ConnectToDataBase.getAllcenters();
                            for (EmergencyCenter emergencyCenter : allcenters) {
                                if (emergencyCenter.getLocation().equals(parts1[2])) {
                                    if (emergencyCenter.getService().equals(parts1[1])) {
                                        DistressCall newdistress = new DistressCall();
                                        newdistress.setRegistered(false);
                                        //newdistress.setUser(user);
                                        newdistress.setDate(LocalDate.now());
                                        newdistress.setLocation(parts1[2]);
                                        newdistress.setTime(LocalTime.now());
                                        //newdistress.setUser_ID(user_id);
                                        newdistress.setEmergencyCenter(emergencyCenter);
                                        ConnectToDataBase.Add_distress(newdistress);
                                        client.sendToClient("The key id is false");
                                        Message update = new Message("update manager distress call list");
                                        update.setObj(newdistress);
                                        sendToAllClients(update);// to update the requests that the client can cancel.
                                        update = new Message("update manager distress call histogram");
                                        update.setObj(newdistress);
                                        sendToAllClients(update);
                                    }
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("The provided key id is not a valid number.");
                    }
                }
            }
            if (message.equals("get tasks")) {
                List<Task> alltasks = ConnectToDataBase.getAllTasks();
                Object[] array = new Object[2];
                array[0] = "alltasks"; // Assign a String object to the first index
                array[1] = alltasks;
                client.sendToClient(array);
            } else if (message.startsWith("Get community members")) {
                String[] parts = message.split("@");
                if (parts.length == 2 && parts[1] != null) {
                    String communityManager = parts[1];
                    // Get the community members based on the community manager's ID
                    List<User> members = ConnectToDataBase.getCommunityMembers(communityManager);
                    // Send the community members list to the client
                    client.sendToClient(members);
                }
            } else if (message.startsWith("Get performed tasks")) {
                String[] parts = message.split("@");
                if (parts.length == 2 && parts[1] != null) {
                    String communityManager = parts[1];
                    // Get the performed tasks based on the community manager's ID
                    List<Task> tasks = ConnectToDataBase.getTasksWithUserCommunityAndStatus(communityManager);
                    if (!tasks.isEmpty()){
                        client.sendToClient(tasks);}
                    else{
                        client.sendToClient("performedtaskisEmpty");
                    }

                }
            } else if (message.startsWith("Get uploaded tasks by community members")) {
                String[] parts = message.split("@");
                if (parts.length == 2 && parts[1] != null) {
                    String communityManager = parts[1];
                    // Get the community members based on the community manager's ID
                    List<Task> requests = ConnectToDataBase.getTasksUploadedByCommunityMembers(communityManager);
                    Object[] array = new Object[2];
                    array[0] = "uploaded"; // Assign a String object to the first index
                    array[1] = requests;
                    client.sendToClient(array);
                    // Send the community members list to the client
                }
            } else if (message.startsWith("check requests@")) {
                String[] parts = message.split("@");
                if (parts.length == 2 && parts[1] != null) {
                    String communityManager = parts[1];
                    // Get the community members based on the community manager's ID
                    List<Task> requests = ConnectToDataBase.getTasksWithStatus(communityManager, 3);
                    Object[] array = new Object[2];
                    array[0] = "request"; // Assign a String object to the first index
                    array[1] = requests;
                    client.sendToClient(array);
                }

            } else if (message.equals("getVolunteerTasks")) {
                List<Task> alltasks = ConnectToDataBase.getAllTasks();
                List<Task> newOne = new ArrayList<>();
                for (Task task : alltasks) {
                    if (task.getStatus() == 0) {
                        newOne.add(task);
                    }
                }
                Object[] array = new Object[2];
                array[0] = "getVolunteerTasks"; // Assign a String object to the first index
                array[1] = newOne;
                client.sendToClient(array);
            } else if (message.equals("getTasks!")) {

            } else if (message.startsWith("myModify")) {
                Message update = new Message("update volunteer");
                String taskid = message.split(" ")[1];
                String volI = message.split(" ")[2];
                myModifyTask(Integer.parseInt(taskid), volI);
                List<Task> alltasks = ConnectToDataBase.getAllTasks();
                List<Task> newOne = new ArrayList<>();
                for (Task task : alltasks) {
                    if (task.getStatus() == 0) {
                        newOne.add(task);
                        update.setObj(task);
                    }
                }
                Object[] array = new Object[2];
                array[0] = "myModify"; // Assign a String object to the first index
                array[1] = newOne;
                client.sendToClient(array);
                sendToAllClients(update);

            } else if (message.startsWith("modTask")) {
                Message update = new Message("complete");
                String taskid = message.split("@")[1];
                String userId = message.split("@")[2];
                modifyCompltedTask(Integer.parseInt(taskid));

                List<Task> volunteers = ConnectToDataBase.getTasksWithVolId(userId);
                List<Task> newOne = new ArrayList<>();
                for (Task task : volunteers) {
                    if (task.getStatus() == 1) {
                        newOne.add(task);
                        update.setObj(task);
                    }
                }
                Object[] array = new Object[2];
                array[0] = "modTask"; // Assign a String object to the first index
                array[1] = newOne;
                client.sendToClient(array);
                sendToAllClients(update);
            } else if (message.startsWith("sendMessage")) {
                String taskid = message.split("@")[1];
                String userId = message.split("@")[2];
                String community = message.split("@")[3];
                LocalDateTime now = LocalDateTime.now();


                String str = String.format("Task ID: %d is performed by user whose ID is: %s", taskid, userId);


            } else if (message.startsWith("modify")) {
                String taskid = message.split(" ")[1];
                modifyTask(Integer.parseInt(taskid));
                client.sendToClient(ConnectToDataBase.getAllTasks());
            } else if (message.startsWith("update data")) {
                System.out.println("server " + UpdateTaskDetails.getUpdateVale());
                String[] parts = message.split("@");
                if (parts.length >= 5 && parts[0].equals("update data")) {
                    String taskId = parts[1];
                    String newData = parts[2];
                    String updateVale = parts[3];
                    String communityManager = parts[4];

                    try {
                        int taskIdInt = Integer.parseInt(taskId);
                        Task task = ConnectToDataBase.getTaskById(taskIdInt);
                        if (task != null) {
                            boolean k = task.getUser().getCommunity().equals(communityManager);
                            if (!k) {
                                client.sendToClient("notInYourCommunity");
                            } else if (task.getStatus() == 5) {
                                client.sendToClient("The task is canceled.");
                            } else if (task.getStatus() == 4) {
                                client.sendToClient("You can't update the status the task has been rejected.");
                            } else if (task.getStatus() != 2 && !updateVale.equals("status")) {
                                client.sendToClient("The task's status isn't 2.");
                            } else if (k) {
                                if ((Integer.parseInt(newData) < 0 || Integer.parseInt(newData) > 5) && updateVale.equals("status")) {
                                    client.sendToClient("the status is illegal");
                                    return;
                                }
                                if (updateVale.equals("status") && (task.getStatus() == 3 || task.getStatus() == 0) && (Integer.parseInt(newData) != 0 || Integer.parseInt(newData) != 1)) {
                                    Message update = new Message("update cancel list");
                                    update.setObj(task);
                                    sendToAllClients(update);// to update the requests that the client can cancel.
                                }
                                ConnectToDataBase.updateTaskData(newData, task, updateVale);
                                client.sendToClient("saved!");
                            }
                        } else {
                            client.sendToClient("notFound");
                            System.out.println("Task with ID " + taskId + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid task ID: " + taskId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (message.startsWith("log out")) {
                String[] parts = message.split(" ");
                if (parts.length == 3 && parts[2] != null) {
                    String id = parts[2];
                    User temp = null;
                    // Update the database with the extracted ID
                    try {
                        ArrayList<User> loggedInList = UserControl.getLoggedInList();
                        for (User user : loggedInList) {
                            if (user.getID().equals(id))
                                temp = user;
                        }
                        ConnectToDataBase.updateIsConnect(false, temp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle invalid message format
                    System.err.println("Invalid 'log out' message format: " + message);
                }
            } else if (message.startsWith("Task is Accept")) {

                Message update = new Message("acceptVolunteer");
                String[] parts = message.split("@");
                if (parts.length >= 2 && parts[0].equals("Task is Accept")) {
                    String taskId = parts[1];
                    try {
                        int taskIdInt = Integer.parseInt(taskId);
                        Task task = ConnectToDataBase.getTaskById(taskIdInt);
                        if (task != null) {
                            ConnectToDataBase.updateTaskData(String.valueOf(0), task, "status");

                            // Schedule timer to check if task is not accepted after 24 hours
                            Sceduler.scheduleTaskVolunteerCheck(taskIdInt);
                            List<Task> requests = ConnectToDataBase.getTasksWithStatus(task.getUser().getCommunity(), 3);
                            Object[] array = new Object[2];
                            array[0] = "accept"; // Assign a String object to the first index
                            array[1] = requests;
                            client.sendToClient(array);
                            update.setObj(task);
                            sendToAllClients(update);

                        } else {
                            System.out.println("Task with ID " + taskId + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid task ID: " + taskId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (message.startsWith("cancel request")) {
                String[] parts = message.split("@");
                if (parts.length >= 2 && parts[0].equals("cancel request")) {
                    String taskId = parts[1];
                    try {
                        int taskIdInt = Integer.parseInt(taskId);
                        Task task = ConnectToDataBase.getTaskById(taskIdInt);
                        if (task != null) {
                            ConnectToDataBase.updateTaskData("5", task, "status");
                            System.out.println("user id " + task.getUser().getID());
                            List<Task> requests = ConnectToDataBase.getTasksWithStatusAndUser(task.getUser().getID());
                            Object[] array = new Object[2];
                            array[0] = "canceled!"; // Assign a String object to the first index
                            array[1] = requests;
                            client.sendToClient(array);
                            String notify = "Task (Id number: " + task.getIdNum() + ") has been canceled";
                            sendToAllClients(task);
                            Warning warning = new Warning(notify);
                            sendToAllClients(warning);
                            Message update = new Message("update uploaded tasks list");
                            update.setObj(task);
                            sendToAllClients(update);// to update the requests that the client can cancel.
                        } else {
                            System.out.println("Task with ID " + taskId + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid task ID: " + taskId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (message.startsWith("confirm volunteer")) {
                String[] parts = message.split("@");
                if (parts.length >= 2 && parts[0].equals("confirm volunteer")) {
                    String taskId = parts[1];
                    String userID = parts[2];
                    try {
                        int taskIdInt = Integer.parseInt(taskId);
                        Task task = ConnectToDataBase.getTaskById(taskIdInt);
                        if (task != null) {
                            ConnectToDataBase.updateTaskData("2", task, "status");
                            System.out.println("user id: " + task.getUser().getID());
                            List<Task> volunteers = ConnectToDataBase.getTasksWithVolId(userID);
                            List<Task> newOne = new ArrayList<>();
                            for (Task tasks : volunteers) {
                                if (tasks.getStatus() == 1) {
                                    newOne.add(tasks);
                                }
                            }
                            Object[] array = new Object[2];
                            array[0] = "confired!"; // Assign a String object to the first index
                            array[1] = newOne;
                            client.sendToClient(array);
                            sendToAllClients(task);

                            Message update = new Message("complete");
                            update.setObj(task);
                            sendToAllClients(update);
                        } else {
                            System.out.println("Task with ID " + taskId + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid task ID: " + taskId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (message.startsWith("The reason of rejected is")) {
                String[] parts = message.split("@");
                String reason = parts[1];
                System.out.println(reason);
                String newData = parts[2];
                System.out.println(newData);
                String news = parts[3];
                List<User> allUsers = ConnectToDataBase.getusersList();
                User manger;
                MessageToUser Message = new MessageToUser();
                Message.setContent(reason);
                String recipientId = news;
                Message.setRecipient(recipientId);
                for (User user : allUsers) {
                    System.out.println("user.getCommunityManager(): " + user.getCommunityManager());
                    System.out.println("newData: " + newData);
                    if (user.getCommunityManager().equals(newData)) {
                        Message.setSender(user.getkeyId());
                    }
                }
                Message.setSentTime(LocalDateTime.now());
                ConnectToDataBase.Add_message(Message);

                client.sendToClient("back to list");

            } else if (message.startsWith("Task is rejected")) {
                String[] parts = message.split("@");
                if (parts.length >= 2 && parts[0].equals("Task is rejected")) {
                    String taskId = parts[1];
                    try {
                        int taskIdInt = Integer.parseInt(taskId);
                        Task task = ConnectToDataBase.getTaskById(taskIdInt);
                        if (task != null) {
                            ConnectToDataBase.updateTaskData(String.valueOf(4), task, "status");
                            List<Task> requests = ConnectToDataBase.getTasksWithStatus(task.getUser().getCommunity(), 3);
                            Object[] array = new Object[2];
                            array[0] = "accept"; // Assign a String object to the first index
                            array[1] = requests;
                            client.sendToClient(array);
                            Message update = new Message("update manager check list");
                            update.setObj(task);
                            sendToAllClients(update);// to update the requests that the client can cancel.
                        } else {
                            System.out.println("Task with ID " + taskId + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid task ID: " + taskId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (message.equals("Get all users")) {
                List<User> allUsers = ConnectToDataBase.getusersList();
                Object[] array = new Object[2];
                array[0] = "all users send"; // Assign a String object to the first index
                array[1] = allUsers;
                client.sendToClient(array);

            } else if (message.startsWith("#LogInAttempt")) {
                try {
                    handleLoginAttempt(message, client);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                List<User> allUsers = ConnectToDataBase.getusersList();
                for (User user : allUsers) {
                    UserControl userControl = new UserControl(user.getID(), user.getFirstName(), user.getLastName(), user.getisConnected(), user.getCommunity(), user.getUsername(), user.getCommunityManager(), "0", user.getAddress(), user.getEmail(), user.getRole());
                    userControl.setSalt(user.getSalt());
                    userControl.setPasswordHash(user.getPasswordHash());
                    userControls.add(userControl);
                }
                boolean isValidLogin = false;
                for (UserControl user : userControls) {
                    isValidLogin = user.login(username, password);

                    if (isValidLogin) {
                        UserControl.addUser(user);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
                            out.writeObject(user);  // assuming 'user' is your User object
                            out.flush();
                        } catch (IOException e) {
                            // Handle serialization exception
                            e.printStackTrace();
                        }
                        if (user.getisConnected()) {
                            client.sendToClient("LOGIN_FAIL2");
                            return;
                        }
                        ConnectToDataBase.updateIsConnect(true, user);

                        byte[] userBytes = bos.toByteArray();
                        client.sendToClient(userBytes);
                        // Send a success response back to the client
                        try {
                            if (username.startsWith("*")) {
                                client.sendToClient("Manager_LOGIN_SUCCESS");
                            } else {
                                client.sendToClient("LOGIN_SUCCESS");
                            }
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