package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageToUser;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Sceduler {
    private static Timer timer = new Timer();

    public static void scheduleTaskVolunteerCheck(int taskId) {
        // Schedule a task to run after 24 hours
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<User> users;
                try {
                    users = ConnectToDataBase.getAllUsers();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                LocalDateTime now = LocalDateTime.now();
                // Check if the task has no volunteer after 24 hours
                Task task = null;
                try {
                    task = ConnectToDataBase.getTaskById(taskId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (task != null && task.getStatus() == 0 && task.getVolId() == null) {
                    // Publish message to all users

                    String str = String.format("task for immediately volunteer: \nTask ID: %d\nTask Description: %s\nUser Name: %s\nUser ID: %s\nState: %d", task.getIdNum(), task.getServiceType(), task.getUser().getFirstName(), task.getUser().getID(), task.getStatus());

                    // Publish the message to all users
                    Long sender = 0L;

                    assert users != null;
                    for (User user : users) {
                        try {
                            String keyId = user.getID();

                            ConnectToDataBase.Add_message(new MessageToUser(str, sender, keyId, now));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }, 24 * 60 * 60 * 1000); // 24 hours in milliseconds
    }
}