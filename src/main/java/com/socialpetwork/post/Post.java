package com.socialpetwork.post;

import com.socialpetwork.user.User;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment feature
    private Long id; //primary key

    @Column(nullable = false, length = 500) // Message body with a set max length of 500 chars
    private String content;

    @Column(nullable = false, updatable = false) // Timestamp, not updatable
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Established many-to-one relationship with User
    private User user;

    // Default Constructor
    public Post() {}

    // Parameterized Constructor
    public Post(String content, User user) {
        this.content = content;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
        this.user = user;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public User getUserId() {
        return user;
    }

    public void setUserId(User user) {
        this.user = user;
    }

    public void setCreatedAt(LocalDateTime now) {
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
