// Can change the naming later. I just do not want to confuse between the actual users that are in the User class
// And the followers, followed users in the Follower class.
// The file might change and more entities added looking at the database later or in process of the finishing project.
// For now, it just a skeleton on which we can build or change more.

package com.socialpetwork.follower;

import com.socialpetwork.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "followers")
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "followed_user_id", nullable = false)
    private User followedUser; // The user who is being followed

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower; // The user who follows

    // Default constructor
    public Follower() {
    }

    // Parameterized constructor
    public Follower(User followedUser, User follower) {
        this.followedUser = followedUser;
        this.follower = follower;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public User getFollowedUser() { return followedUser; }

    public void setFollowedUser(User followedUser) { this.followedUser = followedUser; }

    public User getFollower() { return follower; }

    public void setFollower(User follower) { this.follower = follower; }
}
