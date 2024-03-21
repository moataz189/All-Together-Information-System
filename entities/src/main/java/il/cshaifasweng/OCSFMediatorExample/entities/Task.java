package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tasks")
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNum")
     int idNum=0;

    @Column(name = "date")
     LocalDate date;

    @Column(name = "time")
     LocalTime time;

    @Column(name = "status")
     int status;

    @Column(name = "serviceType")
     String serviceType;

    @Column(name = "note")
     String note;

    @Column(name = "executionTime")
     float executionTime;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
//    @ManyToOne(fetch = FetchType.LAZY) // Assuming you want to use lazy loading
//     UploadedTaskList uploadedTaskList;

    // ... existing constructors, getters, and setters ...

//    public UploadedTaskList getUploadedTaskList() {
//        return uploadedTaskList;
//    }

//    public void setUploadedTaskList(UploadedTaskList uploadedTaskList) {
//        this.uploadedTaskList = uploadedTaskList;
//    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Constructors
    public Task() {
         // Assign the next available ID to the task
    }

    public Task(LocalDate date, LocalTime time, int status, String serviceType, String note, float executionTime) {
        System.out.println("a");
        this.date = date;
        this.time = time;
        this.status = status;
        this.serviceType = serviceType;
        this.note = note;
        this.executionTime = executionTime;
    }

    // Getters and Setters
    public int getIdNum() {
        return idNum;
    }
    public void setIdNum(int idNum) {
        this.idNum = idNum;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }


    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public float getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(float executionTime) {
        this.executionTime = executionTime;
    }
    @Override
    public String toString(){
        return String.format("Task ID: %d\nTask Description: %s\nUser Name: %s\nUser ID: %d\nState: ", this.idNum, this.serviceType,user.getFirstName(),user.getID(), this.status);
    }
}
