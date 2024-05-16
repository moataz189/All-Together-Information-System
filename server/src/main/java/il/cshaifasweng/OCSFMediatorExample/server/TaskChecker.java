package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.MessageToUser;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import il.cshaifasweng.OCSFMediatorExample.entities.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class TaskChecker implements Runnable {
    @Override
    public void run() {
        List<Task> taskList;
        List<User> users;
        try {
            users = ConnectToDataBase.getAllUsers();
            taskList = ConnectToDataBase.getAllTasks();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LocalDateTime now = LocalDateTime.now();
        assert taskList != null;
        for (Task task : taskList) {
            if (task.getStatus() == 0) { // Check if task status is zero
                LocalDateTime taskDateTime = task.getDate().atTime(task.getTime());
                Duration duration = Duration.between(taskDateTime, now);
                long hoursPassed = duration.toHours();
                if (hoursPassed >= 24) {
                    long id = task.getIdNum();
                    String serviceType = task.getServiceType();
                    String firstName = task.getUser().getFirstName();
                    String userid = task.getUser().getID();
                    int status = task.getStatus();

                    String str = String.format("Task for immediately volunteer: \nTask ID: %d\nTask Description: %s\nUser Name: %s\nUser ID: %s\nState: %d", id, serviceType, firstName, userid, status);


                    Long sender = 0L;

                    assert users != null;
                    for (User user : users) {
                        try {
                            //long keyId = user.getkeyId();
                            //String ID = String.valueOf(keyId);
                            String keyId = user.getID();

                            ConnectToDataBase.Add_message(new MessageToUser(str, sender,keyId, now));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }
}
