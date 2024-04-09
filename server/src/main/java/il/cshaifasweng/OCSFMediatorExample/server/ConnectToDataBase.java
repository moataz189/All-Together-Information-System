package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.client.NewTaskDataControl;
import il.cshaifasweng.OCSFMediatorExample.client.UpdateTaskDetails;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConnectToDataBase {
    private static Session session;
    private static List<User> users;

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Task.class);
        configuration.addAnnotatedClass(UserControl.class);
//        configuration.addAnnotatedClass(UploadedTaskList.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    static List<User> getAllUsers() throws Exception {
        try {
            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("getALlUsers");
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            query.from(User.class);
            List<User> data = session.createQuery(query).getResultList();
            return data;
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback transaction if an exception occurs
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    static List<User> getCommunityMembers(String community) throws Exception {
        try {
            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("Getting community members for: " + community);
            // Assuming 'session' is your Hibernate session object
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            // Creating a query root for the User entity
            Root<User> root = query.from(User.class);

            // Adding a condition to filter users by community
            query.where(builder.equal(root.get("community"), community));
            // Executing the query and getting the list of users
            List<User> users = session.createQuery(query).getResultList();
            return users;
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback transaction if an exception occurs
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public static List<Task> getTasksUploadedByCommunityMembers(String community) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("Getting community tasks for: " + community);
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);

            // Joining the Task entity with the User entity to access the uploader
            Join<Task, User> uploaderJoin = root.join("user");

            // Adding a condition to filter tasks uploaded by community members
            query.where(builder.equal(uploaderJoin.get("community"), community));

            // Executing the query and getting the list of tasks
            List<Task> tasks = session.createQuery(query).getResultList();
            return tasks;
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback transaction if an exception occurs
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public static List<Task> getTasksWithStatus(String community, int status) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("Getting community tasks for: " + community + " with status: " + status);
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            // Joining the Task entity with the User entity to access the uploader
            Join<Task, User> uploaderJoin = root.join("user");
            query.where(
                    builder.equal(uploaderJoin.get("community"), community),
                    builder.equal(root.get("status"), status)
            );
            // Executing the query and getting the list of tasks
            List<Task> tasks = session.createQuery(query).getResultList();
            //System.out.println("try ++ "+tasks.get(0).getTime());
            return tasks;
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback transaction if an exception occurs
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }


    static List<Task> getAllTasks() throws Exception {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            query.from(Task.class);
            List<Task> data = session.createQuery(query).getResultList();
            return data;
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback transaction if an exception occurs
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    static Task getTaskById(int id) throws Exception {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("Getting task with id: " + id);
            // Assuming 'session' is your Hibernate session object
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            // Creating a query root for the Task entity
            Root<Task> root = query.from(Task.class);
            // Adding a condition to filter tasks by id
            query.where(builder.equal(root.get("idNum"), id));
            // Executing the query and getting the task
            Task task = session.createQuery(query).uniqueResult();
            return task;
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback transaction if an exception occurs
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }


    public static void updateTaskData(int id, String newData, Task task,String updateValue) throws Exception {
        SessionFactory sessionFactory = getSessionFactory();
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            if (task != null) {
                if (updateValue.equals("status")) {
                    task.setStatus(Integer.parseInt(newData));
                    session.update(task);
                    session.flush();
                    session.getTransaction().commit();
                    System.out.println("Task status updated successfully.");
                } else {
                    task.setExecutionTime(Float.parseFloat(newData));
                    session.update(task);
                    session.flush();
                    session.getTransaction().commit();
                    System.out.println("Task execution time updated successfully.");
                }
            } else {

                System.out.println("Task not found.");
            }
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    static void addTask(Task task) throws Exception {
        User temp = null;
        try {
            users = getAllUsers();
            for (User user : users) {
                if (user.getID().equals(UserControl.getLoggedInUser().getID())) {
                    temp = user;
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println("try to add task to database");
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            task.setUser(temp);
            temp.getTasks().add(task); // Add the task to the user's list of tasks
            session.save(task); // Save the task
            // Update the user entity
            session.update(temp);
            session.flush(); // Flush changes to session
            session.getTransaction().commit(); // Commit transaction
            System.out.println("Task added successfully to the database.");
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback transaction if an exception occurs
            }
            System.err.println("Failed to add task to the database.");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public static void CreateData() throws Exception {
        System.out.print("Data Creation Start");
        User user1 = new User("212930690", "Qamar", "Hammod", false, "Community 1", "*1", "Community 1", "2", "Kabul", "qammar@gmail.com", User.Role.Manager);
        User user7 = new User("215630125", "Moataz", "Odeh", false, "Community 1", "MoatazOD", "?", "MoatazO123", "Yaffa Nazareth", "Moataz.ody44@gmail.com", User.Role.USER);
        User user8 = new User("345869321", "Aya", "Hammod", false, "Community 7", "22", "?", "2", "Yaffa Nazareth", "ayaaa@gmail.com", User.Role.USER);
        User user2 = new User("213011398", "Adan", "Hammoud", false, "Community 1", "AdanHa", "?", "adan123", "Kabul ", "Adanhammod@gmail.com", User.Role.USER);
        User user3 = new User("213298664", "Adan", "Sulaimani", false, "Community 3", "AdanSul", "?", "AdanS123", "Nazareth", "Adaslemany@gmail.com", User.Role.USER);
        User user4 = new User("212022263", "Mohammed", "Shhade", false, "Community 2", "MoShhade", "?", "MoShhade123", "Kukab", "hijaze.Najm@gmail.com", User.Role.USER);
        User user5 = new User("212270565", "Nejem", "Higazy", false, "Community 2", "NejemH", "?", "NejemH123", "Tamra", "Muhammed.sh.181@gmail.com", User.Role.USER);
        User user6 = new User("319050241", "Siraj", "Jabareen", false, "Community 6", "SirajJ", "?", "SirajJ123", "UMM El Fahem", "SerajWazza@gmail.com", User.Role.USER);
        User user9 = new User("213047896", "Shams", "Hmam", false, "Community 8", "*9", "Community 2", "3", "Tamra", "shams@gmail.com", User.Role.Manager);
        User user10 = new User("212589631", "Ahmad", "Hammod", false, "Community 8", "ahmad", "?", "3", "kabul", "ahmad@gmail.com", User.Role.USER);
        User user11 = new User("213470213", "Mahmod", "Hammod", false, "Community 3", "mahmod", "?", "3", "Kabul", "mahmod@gmail.com", User.Role.USER);
        User user12 = new User("312568965", "Seren", "Jamal", false, "Community 2", "soso", "?", "3", "Kabul", "seren1@gmail.com", User.Role.USER);

        session.save(user1);
        session.flush();
        session.save(user2);
        session.flush();
        session.save(user3);
        session.flush();
        session.save(user4);
        session.flush();
        session.save(user5);
        session.flush();
        session.save(user6);
        session.flush();
        session.save(user7);
        session.flush();
        session.save(user8);
        session.flush();
        session.save(user9);
        session.flush();
        session.save(user10);
        session.flush();
        session.save(user11);
        session.flush();
        session.save(user12);
        session.flush();
        Task task1 = new Task(LocalDate.of(2024, 2, 22), LocalTime.of(3, 50), 2, "Walk my dog", "", 0.0f);
        task1.setUser(user3);
        Task task2 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(9, 30), 0, "Buy Medicine", "", 0.0f);
        task2.setUser(user8);
        Task task3 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(11, 15), 3, "Nanny", "", 0.0f);
        Task task4 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(13, 4), 3, "Transportation", "I want to go to the Hospital", 0.0f);
        Task task5 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(15, 20), 0, "Transportation", "", 0.0f);
        Task task6 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(17, 10), 0, "Buy Medicine", "", 0.0f);
        user10.getTasks().add(task1);
        user2.getTasks().add(task2);
        task1.setUser(user1);
        user3.getTasks().add(task3);
        user4.getTasks().add(task4);
        user5.getTasks().add(task5);
        user6.getTasks().add(task6);
        task2.setUser(user2);
        task3.setUser(user3);
        task4.setUser(user4);
        task5.setUser(user5);
        task6.setUser(user6);


        session.save(task1);
        session.flush();
        System.out.println(LocalTime.now().getHour());
        session.save(task2);
        session.flush();
        session.save(task3);
        session.flush();
        session.save(task4);
        session.flush();
        session.save(task5);
        session.flush();
        session.save(task6);
        session.flush();

        session.update(user1);
        session.flush();
        System.out.println(LocalTime.now().getHour());
        session.update(user2);
        session.flush();
        session.update(user3);
        session.flush();
        session.save(user4);
        session.flush();
        session.save(user5);
        session.flush();
        session.save(user6);
        session.flush();
        System.out.print("Data Creation Finish");

    }

    public static void initializeDatabase() throws IOException {
        try {
            if (!getAllTasks().isEmpty() && !getAllUsers().isEmpty()) {
                users = getAllUsers();
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("MOTAZ");
            CreateData();
            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        try {
            users = getAllUsers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}