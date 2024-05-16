package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.LocalDate;
@Entity
@Table(name = "DistressCall")
public class DistressCall implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isRegistered;
    private LocalTime time;
    private String location;
    private LocalDate date;
    @ManyToOne
    private EmergencyCenter emergencyCenter;
    private String User_ID;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user")
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;

    }
    public DistressCall(boolean isRegistered, LocalTime time, String location, LocalDate date, String User_ID, EmergencyCenter emergencyCenter, User user) {
        this.isRegistered = isRegistered;
        this.time = time;
        this.location = location;
        this.date = date;
        this.User_ID = User_ID;
        this.emergencyCenter = emergencyCenter;
        this.user = user;
    }


    // Constructor
    public DistressCall(boolean isRegistered, LocalTime time, String location, LocalDate date,String id) {
        this.isRegistered = isRegistered;
        this.time = time;
        this.location = location;
        this.date = date;
        this.User_ID=id;
    }

    public DistressCall() {
    }

    // Getters and setters
    public boolean isRegistered() {
        return isRegistered;
    }
    public EmergencyCenter getEmergencyCenter() {
        return emergencyCenter;
    }

    public void setEmergencyCenter(EmergencyCenter emergencyCenter) {
        this.emergencyCenter = emergencyCenter;
    }


    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getid() {
        return User_ID;
    }

    public void setUser_ID(String id) {
        this.User_ID = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // toString method for easy object representation
    @Override
    public String toString() {
        return "DistressCall{" +
                "isRegistered=" + isRegistered +
                ", time=" + time +
                ", location='" + location + '\'' +
                ", date=" + date +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}