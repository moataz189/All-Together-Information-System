package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "messages")

public class MessageToUser implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "sender_id", nullable = false)
    private Long sender;

    @Column(name = "recipient_id", nullable = false)
    private Long recipient;
    @Column(name = "sent_time")
    private LocalDateTime sentTime; // Use the LocalDateTime class




    // Constructor
    public MessageToUser(String content, Long sender, Long recipient, LocalDateTime sentTime) {
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.sentTime = sentTime; // Initialize the sentTime
    }


    public MessageToUser() {

    }
    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getRecipient() {
        return recipient;
    }

    public void setRecipient(Long recipient) {
        this.recipient = recipient;
    }




    // toString method for easy printing


}

