package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;
    @Column(name = "User_ID", nullable = false)
     String Id;
    @Column(name = "First_Name", nullable = false)
     String first_name;
    @Column(name = "Last_Name", nullable = false)
     String last_name;

    @Column(name = "is_connected")
     boolean isConnected;

    @Column(name = "community")
     String community;

    @Column(name = "username", nullable = false, unique = true)
     String username;

    @Column(name = "password_hash")
     String passwordHash;

    @Column(name = "salt")
     String salt;

    @Column(name = "address")
     String address;
    @Column(name = "Email")
     String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
     Role role;
     String password;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    Set<Task> tasks=new HashSet<>();

    // Constructor, getters, and setters...

    public Set<Task> getTasks() {
        return tasks;
    }
    public Set<Task> getTreatments() {
        return tasks;
    }

    public User(String id,String first_name,String
                Last_name,boolean isConnected, String community,
                String username, String Password, String address,String emil, Role role) {
        this.Id=id;
        this.first_name = first_name;
        this.last_name=Last_name;
        this.isConnected = isConnected;
        this.community = community;
        this.username = username;
        this.salt = generateSalt();
        this.password=Password;
        this.passwordHash = hashPassword(Password, this.salt);
        this.address = address;
        this.email=emil;
        this.role = role;

    }
    public User(){
        tasks = new HashSet<>();
    }
    public enum Role {
        Manager, USER
        // Define other roles as needed
    }

    // Generate a salt
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }

     String hashPassword(String password, String salt) {
        try {
            String passwordWithSalt = password + salt;
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedPassword = md.digest(passwordWithSalt.getBytes());
            return bytesToHex(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception
            return null;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    public Long getkeyId() {
        return id;
    }
    public String getID() {
        return Id;
    }
    public void setPas(String Pas){
        this.password=Pas;
    }
    public String getPassword() {
        return this.password;
    }
    public void setId(String ID){
        this.Id=ID;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setfirstName(String name) {
        this.first_name = name;
    }
    public String getLastName() {
        return last_name;
    }
    public void setemail(String email) {
        this.email =email;
    }
    public String getEmail() {
        return email;
    }

    public void setlastName(String name) {
        this.last_name = name;
    }

    public boolean getisConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.salt = generateSalt();
        this.passwordHash = hashPassword(password, this.salt);
    }
}

