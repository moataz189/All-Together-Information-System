package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConnectToDataBase {
    private static Session session;
    private static List<User> users;

    public static ArrayList<User> getusersList() {
        return (ArrayList<User>) users;
    }

    // Set method to modify the allUsers list
    public static void setAllUsers(ArrayList<User> usersList) {
        users = usersList;
    }

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Task.class);
        configuration.addAnnotatedClass(UserControl.class);
        configuration.addAnnotatedClass(MessageToUser.class);
        configuration.addAnnotatedClass(DistressCall.class);
        configuration.addAnnotatedClass(EmergencyCenter.class);
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

    static List<Task> getTasksWithUserCommunityAndStatus(String community) throws Exception {
        try {
            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("Getting tasks with user community matching: " + community + " and status equal to 2");

            // Assuming 'session' is your Hibernate session object
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);

            // Creating a query root for the Task entity
            Root<Task> taskRoot = query.from(Task.class);

            // Fetching the user ID from the Task entity
            Expression<Integer> volIdExpression = taskRoot.get("vol_Id");

            // Creating a subquery to fetch the user's community
            Subquery<String> subquery = query.subquery(String.class);
            Root<User> userRoot = subquery.from(User.class);
            subquery.select(userRoot.get("community"));
            subquery.where(builder.equal(userRoot.get("Id"), volIdExpression));

            // Adding conditions to filter tasks by user community and status equal to 2
            query.where(
                    builder.equal(subquery, community),
                    builder.equal(taskRoot.get("status"), 2)
            );

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

    public static List<DistressCall> getDistressCallsBetweenDates(String community, LocalDate targetDate) {
        Session session = null;
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<DistressCall> query = builder.createQuery(DistressCall.class);
            Root<DistressCall> root = query.from(DistressCall.class);

            Join<DistressCall, User> userJoin = root.join("user", JoinType.LEFT);

            LocalDate currentDate = LocalDate.now();

            query.select(root).where(
                    builder.and(
                            builder.equal(userJoin.get("community"), community),
                            builder.between(root.get("date"), targetDate, currentDate)
                    )
            );

            List<DistressCall> distressCalls = session.createQuery(query).getResultList();

            transaction.commit();
            return distressCalls;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
                e.printStackTrace();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    static List<EmergencyCenter> getAllcenters() throws Exception {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<EmergencyCenter> query = builder.createQuery(EmergencyCenter.class);
            query.from(EmergencyCenter.class);
            List<EmergencyCenter> data = session.createQuery(query).getResultList();
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

    static void Add_distress(DistressCall distressCall) throws Exception {
        try {
            System.out.println("Trying to add a distresscall to the database...");
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.save(distressCall); // Save the message

            session.flush(); // Flush changes to the session
            session.getTransaction().commit(); // Commit the transaction

            System.out.println("distresscall added successfully to the database.");
        } catch (HibernateException e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback the transaction if an exception occurs during the transaction
            }
            // Log the exception or handle it accordingly
            System.err.println("Error adding distresscall to the database: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close(); // Close the session
            }
        }
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

    public static List<Task> getTasksWithStatusAndUser(String userId) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            // Joining the Task entity with the User entity to access the uploader
            Join<Task, User> userJoin = root.join("user");
            query.where(
                    builder.and(
                            builder.or(
                                    builder.equal(root.get("status"), 0),
                                    builder.equal(root.get("status"), 3)
                            ),
                            builder.equal(userJoin.get("Id"), userId)
                    )
            );
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

    public static List<Task> getTasksWithVolId(String userId) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);
            // Adding condition for vol_Id
            query.where(builder.equal(root.get("vol_Id"), userId));
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

    static List<Task> getTasksForVolunteerInCommunity(String community, String userId) throws Exception {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("Getting tasks for volunteer: " + userId + " in community: " + community);

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Task> query = builder.createQuery(Task.class);
            Root<Task> root = query.from(Task.class);

            // Adding conditions to filter tasks by community and volunteer ID
            query.where(
                    builder.and(
                            builder.equal(root.get("community"), community),
                            builder.equal(root.get("vol_Id"), userId)
                    )
            );

            List<Task> tasks = session.createQuery(query).getResultList();
            return tasks;
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
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


    public static List<DistressCall> getallDistressCallsBetweenDates(LocalDate targetDate) {
        Session session = null;
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<DistressCall> query = builder.createQuery(DistressCall.class);
            Root<DistressCall> root = query.from(DistressCall.class);
            Join<DistressCall, User> userJoin = root.join("user", JoinType.LEFT);
            LocalDate currentDate = LocalDate.now();
            query.select(root).where(
                    builder.and(
                            builder.between(root.get("date"), targetDate, currentDate)
                    )
            );
            List<DistressCall> distressCalls = session.createQuery(query).getResultList();

            return distressCalls;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
                e.printStackTrace();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public static void updateTaskData(String newData, Task task, String updateValue) throws Exception {
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

    public static void updateTaskVolunteerId(String newVolunteerId, Task task) {
        Session session = null;
        Transaction transaction = null;

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            if (task != null) {
                task.setVolId(newVolunteerId);
                session.update(task);
                transaction.commit();
                System.out.println("Task volunteer id updated successfully.");
            } else {
                System.out.println("Task not found.");
            }
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public static List<MessageToUser> getMessagesBySender(String senderId) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("Getting data related to sender: " + senderId);
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MessageToUser> query = builder.createQuery(MessageToUser.class);
            Root<MessageToUser> root = query.from(MessageToUser.class);

            // Adding a condition to filter data based on sender ID
            query.where(builder.equal(root.get("recipient"), senderId));

            // Execute the query and get the list of related data
            List<MessageToUser> dataList = session.createQuery(query).getResultList();
            return dataList;
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

    public static List<MessageToUser> getMessagesByRecipient(String recipientId) {
        Session session = null;
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("Getting data related to recipient: " + recipientId);
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MessageToUser> query = builder.createQuery(MessageToUser.class);
            Root<MessageToUser> root = query.from(MessageToUser.class);

            // Adding a condition to filter data based on recipient ID
            query.where(builder.equal(root.get("recipient"), recipientId));

            // Execute the query and get the list of related data
            List<MessageToUser> dataList = session.createQuery(query).getResultList();
            return dataList;
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

    public static void deleteMessagesByTaskId(String taskId) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaDelete<MessageToUser> deleteQuery = builder.createCriteriaDelete(MessageToUser.class);
            Root<MessageToUser> root = deleteQuery.from(MessageToUser.class);

            // Adding a condition to delete all messages containing the task ID in their content
            deleteQuery.where(builder.like(root.get("content"), "%" + "Task ID: " + taskId + "%"));

            int deletedCount = session.createQuery(deleteQuery).executeUpdate();
            System.out.println("Number of messages deleted: " + deletedCount);

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static void updateIsConnect(boolean newVal, User userLog) throws Exception {
        SessionFactory sessionFactory = getSessionFactory();
        User temp = null;
        try {
            //List<User> users = getAllUsers(); // Assuming getAllUsers returns all users
            for (User user : getusersList()) {
                if (user.getID().equals(userLog.getID())) {
                    temp = user;
                    break;
                }
            }
            System.out.println("Number of users retrieved: " + users.size());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving users", e);
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            if (temp != null) {
                temp.setConnected(newVal);
                session.update(temp);
                session.flush();
                session.getTransaction().commit();
                System.out.println("User connection status updated successfully: " + temp.getUsername());
            } else {
                System.out.println("User not found: " + userLog.getUsername());
            }
        } catch (Exception e) {
            System.out.println("Error updating user connection status: " + e.getMessage());
            throw e; // Rethrow the exception after logging
        }finally {
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
                if (user.getID().equals(task.getUser().getID())) {
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
            task.setVolId("");
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

    static void Add_message(MessageToUser message) throws Exception {
        try {
            System.out.println("Trying to add a message to the database...");
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(message); // Save the message
            session.flush(); // Flush changes to the session
            session.getTransaction().commit(); // Commit the transaction

            System.out.println("Message added successfully to the database.");
        } catch (HibernateException e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback the transaction if an exception occurs during the transaction
            }
            // Log the exception or handle it accordingly
            System.err.println("Error adding message to the database: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close(); // Close the session
            }
        }
    }

    public static void CreateData() throws Exception {

        System.out.print("Data Creation Start");
        User user1 = new User("212930690", "Qamar", "Hammod", false, "Community 1", "*1", "Community 1", "2", "Kabul", "qammar@gmail.com", User.Role.Manager);
        User user2 = new User("213011398", "Adan", "Hammoud", false, "Community 1", "AdanHa", "?", "adan123", "Kabul ", "Adanhammod@gmail.com", User.Role.USER);
        User user3 = new User("213298664", "Adan", "Sulaimani", false, "Community 3", "AdanSul", "?", "AdanS123", "Nazareth", "Adaslemany@gmail.com", User.Role.USER);
        User user4 = new User("212022263", "Mohammed", "Shhade", false, "Community 2", "MoShhade", "?", "MoShhade123", "Kukab", "Muhammed.sh.181@gmail.com", User.Role.USER);
        User user5 = new User("212270565", "Nejem", "Higazy", false, "Community 2", "NejemH", "?", "NejemH123", "Tamra", "hijaze.Najm@gmail.com", User.Role.USER);
        User user6 = new User("319050241", "Siraj", "Jabareen", false, "Community 6", "SirajJ", "?", "SirajJ123", "UMM El Fahem", "SerajWazza@gmail.com", User.Role.USER);
        User user7 = new User("215630125", "Moataz", "Odeh", false, "Community 1", "MoatazOD", "?", "MoatazO123", "Yaffa Nazareth", "Moataz.ody44@gmail.com", User.Role.USER);
        User user8 = new User("345869321", "Aya", "Hammod", false, "Community 1", "22", "?", "2", "Yaffa Nazareth", "ayaaa@gmail.com", User.Role.USER);
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
        Task task1 = new Task(LocalDate.of(2024, 2, 22), LocalTime.of(3, 50), 2, "215630125", "Walk my dog", "", 0.0f);
        task1.setUser(user2);
        Task task2 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(9, 30), 3, "", "Buy Medicine", "", 0.0f);
        Task task3 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(11, 15), 3, "", "Nanny", "", 0.0f);
        Task task4 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(13, 4), 3, "", "Transportation", "I want to go to the Hospital", 0.0f);
        Task task5 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(15, 20), 2, "", "Transportation", "", 0.0f);
        Task task6 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(17, 10), 0, "", "Buy Medicine", "", 0.0f);
        Task task7 = new Task(LocalDate.of(2024, 4, 15), LocalTime.of(17, 15), 0, "", "Home cleaning", "", 0.0f);
        user2.getTasks().add(task1);
        user7.getTasks().add(task2);
        user3.getTasks().add(task3);
        user4.getTasks().add(task4);
        user5.getTasks().add(task5);
        user6.getTasks().add(task6);
        user10.getTasks().add(task7);

        task2.setUser(user7);
        //task3.setUser(user3);
        task3.setUser(user7);
        task4.setUser(user4);
        task5.setUser(user5);
        task6.setUser(user6);
        task7.setUser(user10);

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
        session.update(user4);
        session.flush();
        session.update(user5);
        session.flush();
        session.update(user6);
        session.flush();
        session.update(user7);
        session.flush();
        EmergencyCenter center16 = new EmergencyCenter("123-456-7390", 8.0f, 20.0f, "UMM El Fahem", "Ambulance Alert");
        EmergencyCenter center17 = new EmergencyCenter("123-456-7489", 8.0f, 20.0f, "Yaffa Nazareth", "Police Alret");
        EmergencyCenter center18 = new EmergencyCenter("123-456-7192", 8.0f, 20.0f, "Yaffa Nazareth", "Fire Alret");
        EmergencyCenter center1 = new EmergencyCenter("123-456-7890", 8.0f, 20.0f, "Yaffa Nazareth", "Ambulance Alert");
        EmergencyCenter center2 = new EmergencyCenter("123-456-7891", 8.0f, 20.0f, "Yaffa Nazareth", "Police Alret");
        EmergencyCenter center3 = new EmergencyCenter("123-456-7892", 8.0f, 20.0f, "Yaffa Nazareth", "Fire Alret");
        EmergencyCenter center4 = new EmergencyCenter("123-456-7893", 8.0f, 20.0f, "Kabul", "Ambulance Alert");
        EmergencyCenter center5 = new EmergencyCenter("123-456-7894", 8.0f, 20.0f, "Kabul", "Police Alret");
        EmergencyCenter center6 = new EmergencyCenter("123-456-7895", 8.0f, 20.0f, "Kabul", "Fire Alret");
        EmergencyCenter center7 = new EmergencyCenter("123-456-7896", 8.0f, 20.0f, "Nazareth", "Ambulance Alert");
        EmergencyCenter center8 = new EmergencyCenter("123-456-7897", 8.0f, 20.0f, "Nazareth", "Police Alret");
        EmergencyCenter center9 = new EmergencyCenter("123-456-7898", 8.0f, 20.0f, "Nazareth", "Fire Alret");
        EmergencyCenter center10 = new EmergencyCenter("123-456-7899", 8.0f, 20.0f, "Tamra", "Ambulance Alert");
        EmergencyCenter center11 = new EmergencyCenter("123-456-7840", 8.0f, 20.0f, "Tamra", "Police Alret");
        EmergencyCenter center12 = new EmergencyCenter("123-456-7830", 8.0f, 20.0f, "Tamra", "Fire Alret");
        EmergencyCenter center13 = new EmergencyCenter("123-456-7810", 8.0f, 20.0f, "Kukab", "Ambulance Alert");
        EmergencyCenter center14 = new EmergencyCenter("123-456-7820", 8.0f, 20.0f, "Kukab", "Police Alret");
        EmergencyCenter center15 = new EmergencyCenter("123-456-7805", 8.0f, 20.0f, "Kukab", "Fire Alret");


        session.save(center15);
        session.flush();
        session.save(center14);
        session.flush();
        session.save(center13);
        session.flush();
        session.save(center12);
        session.flush();
        session.save(center11);
        session.flush();
        session.save(center10);
        session.flush();
        session.save(center9);
        session.flush();
        session.save(center8);
        session.flush();
        session.save(center7);
        session.flush();
        session.save(center6);
        session.flush();
        session.save(center5);
        session.flush();
        session.save(center4);
        session.flush();
        session.save(center3);
        session.flush();
        session.save(center2);
        session.flush();
        session.save(center1);
        session.flush();
        DistressCall distressCall1 = new DistressCall(true, LocalTime.of(13, 4), "Tamra", LocalDate.of(2024, 4, 21), "345869321", center1, user8);
        session.save(distressCall1);
        session.flush();
        DistressCall distressCall2 = new DistressCall(true, LocalTime.of(13, 28), "Tamra", LocalDate.of(2024, 4, 21), "213011398", center2, user2);
        session.save(distressCall2);
        session.flush();
        DistressCall distressCall3 = new DistressCall(true, LocalTime.of(18, 34), "Tamra", LocalDate.of(2024, 5, 2), "213011398", center3, user2);
        session.save(distressCall3);
        session.flush();
        DistressCall distressCall4 = new DistressCall(true, LocalTime.of(10, 30), "Yaffa Nazareth", LocalDate.of(2024, 4, 21), "215630125", center5, user7);
        session.save(distressCall4);
        session.flush();
        DistressCall distressCall5 = new DistressCall(true, LocalTime.of(9, 14), "Nazareth", LocalDate.of(2024, 4, 29), "213011398", center6, user2);
        session.save(distressCall5);
        session.flush();
        DistressCall distressCall6 = new DistressCall(true, LocalTime.of(17, 40), "Yaffa Nazareth", LocalDate.of(2024, 5, 1), "215630125", center7, user7);
        session.save(distressCall6);
        session.flush();


        System.out.print("Data Creation Finish");

    }

    public static void initializeDatabase() throws IOException {
        try {
            List<Task> tasks = getAllTasks();
            List<User> usersList = getAllUsers();

            if (tasks != null && !tasks.isEmpty() && usersList != null && !usersList.isEmpty()) {
                users = usersList;
                setAllUsers((ArrayList<User>) usersList);
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
            session.clear();

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