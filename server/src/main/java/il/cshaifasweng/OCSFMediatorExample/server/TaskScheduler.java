package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageToUser;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class TaskScheduler {
    private static Timer timer = new Timer();

    public static void scheduleTaskReminder(int taskId, String volunteerId) {
        // Schedule a task to run after 24 hours
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Check if the task is still in status 1
                Task task = null;
                try {
                    task = ConnectToDataBase.getTaskById(taskId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (task != null && task.getStatus() == 1) {
                    // Send reminder to the volunteer
                    sendReminderNotification(taskId,volunteerId);
                }
            }
        }, 24 * 60 * 60 * 1000); // 24 hours in milliseconds,  1 * 60 * 1000 - 1 minute in milliseconds
    }

    private static void sendReminderNotification(int taskID,String volunteerId) {
        LocalDateTime now = LocalDateTime.now();
        //String reminderMessage = "Reminder: You have a task pending. Please confirm if the task has been completed.";
        String reminderMessage = "Reminder-> Task ID: " + taskID + " is pending, Please confirm if the task has been completed.";
        Long sender = 0L;

        try {
            // Add the reminder message to the database
            ConnectToDataBase.Add_message(new MessageToUser(reminderMessage, sender, volunteerId, now));
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}