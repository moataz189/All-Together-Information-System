package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println("getALlUsers");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        query.from(User.class);
        List<User> data = session.createQuery(query).getResultList();
        return data;
    }

    static List<Task> getAllTasks() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Task> query = builder.createQuery(Task.class);
        query.from(Task.class);
        List<Task> data = session.createQuery(query).getResultList();
        return data;
    }
    public static void CreateData() throws Exception {
        System.out.print("Data Creation Start");
        User user1 = new User("212930697", "Moataz", "Odeh", false, "Community 1", "1", "2", "Yaffa Nazareth","Moataz.ody44@gmail.com", User.Role.Manager);
        User user7 = new User("212930697", "Moataz", "Odeh", false, "Community 1", "MoatazOD", "MoatazO123", "Yaffa Nazareth","Moataz.ody44@gmail.com", User.Role.USER);
        User user2 = new User("213011398", "Adan", "Hammoud", false, "Community 2", "AdanHa", "AdanH123", " Kabul ","Adanhammod@gmail.com", User.Role.USER);
        User user3 = new User("213298664", "Adan", "Sulaimani", false, "Community 3", "AdanSul", "AdanS123", "Nazareth","Adaslemany@gmail.com" ,User.Role.USER);
        User user4 = new User("212022263", "Mohammed", "Shhade", false, "Community 4", "MoShhade", "MoShhade123", "Kukab","hijaze.Najm@gmail.com" ,User.Role.USER);
        User user5 = new User("212270565", "Nejem", "Higazy", false, "Community 5", "NejemH", "NejemH123", "Tamra","Muhammed.sh.181@gmail.com", User.Role.USER);
        User user6 = new User("319050241", "Siraj", "Jabareen", false, "Community 6", "SirajJ", "SirajJ123", "UMM El Fahem","SerajWazza@gmail.com", User.Role.USER);
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
        Task task1 = new Task(LocalDate.of(2024, 2, 22), LocalTime.of(3 ,50), 0, "Walk my dog", "", 0.0f);
        Task task2 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(9, 30), 0, "Buy Medicine", "", 0.0f);
        Task task3 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(11, 15), 0, "Nanny", "", 0.0f);
        Task task4 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(13, 4), 0, "Transportation", "I want to go to the Hospital", 0.0f);
        Task task5 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(15, 20), 0, "Transportation", "", 0.0f);
        Task task6 = new Task(LocalDate.of(2024, 2, 21), LocalTime.of(17, 10), 0, "Buy Medicine", "", 0.0f);
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

        user1.getTasks().add(task1);
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
//        UploadedTaskList uploadedTaskList = new UploadedTaskList();
//        uploadedTaskList.getTasks().add(task1);
//        uploadedTaskList.getTasks().add(task2);
//        uploadedTaskList.getTasks().add(task3);
//        uploadedTaskList.getTasks().add(task4);
//        uploadedTaskList.getTasks().add(task5);
//        uploadedTaskList.getTasks().add(task6);
        System.out.print("Data Creation Finish");

    }
    static void addTask(Task task) throws Exception {
        try {
            // session.beginTransaction(); // Begin transaction
            if (task.getUser() != null){
                session.save(task.getUser());
            }
            session.save(task);
            // session.getTransaction().commit(); // Commit transaction after saving
            session.flush();
            System.out.println("Task added successfully to the database.");
        } catch (Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Rollback transaction if an exception occurs
            }
            System.err.println("Failed to add task to the database.");
            e.printStackTrace();
        }
    }
    public static void initializeDatabase() throws IOException
    {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("MOTAZ");
            CreateData();
            users = getAllUsers();
            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        }
    }
    public static void EndConnection(){
        session.getTransaction().commit(); // Save everything.
        session.close();
    }

}
