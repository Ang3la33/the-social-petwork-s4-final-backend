package com.socialpetwork.user;

// NOTE: Entity
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socialpetwork.follower.Follower;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String birthday;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    private String profileUrl;

    // Constructor
    public User(){}

    // Parameterized Constructor
    public User(String name, String birthday, String email,  String username, String password) {
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Relationships logic
    // User following user
    @OneToMany(mappedBy = "followedUser", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Follower> followers = new HashSet<>();

    //user this user follows
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follower> following = new HashSet<>();

    // Getters and Setters
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getBirthday(){
        return birthday;
    }

    public void setBirthday(String birthday){
        this.birthday = birthday;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Set<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Follower> followers) {
        this.followers = followers;
    }

    public Set<Follower> getFollowing() {
        return following;
    }

    public void setFollowing(Set<Follower> following) {
        this.following = following;
    }
}