package com.socialpetwork.follower;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socialpetwork.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "followed_user_id", nullable = false)
    @JsonIgnore
    private User followedUser; // The user who is being followed

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follower_id", nullable = false)
    @JsonIgnore
    private User follower; // The user who follows

    @Column(nullable = false)
    private LocalDateTime followedAt;

    // Default constructor
    public Follower() {
    }

    // Parameterized constructor
    public Follower(User followedUser, User follower) {
        this.followedUser = followedUser;
        this.follower = follower;
        this.followedAt = LocalDateTime.now(); // Automatically sets timestamp
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollowedUser() {
        return followedUser;
    }

    public void setFollowedUser(User followedUser) {
        this.followedUser = followedUser;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(LocalDateTime followedAt) {
        this.followedAt = followedAt;
    }
}
