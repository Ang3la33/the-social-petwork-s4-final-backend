package com.socialpetwork.follower;

import com.socialpetwork.user.User;
import com.socialpetwork.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    public FollowerService(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    // Follow a user
    public String followUser(Long followerId, Long followedUserId) {
        if (followerRepository.existsByFollowedUserIdAndFollowerId(followedUserId, followerId)) {
            return "❌ You are already following this user.";
        }

        Optional<User> followedUser = userRepository.findById(followedUserId);
        Optional<User> follower = userRepository.findById(followerId);

        if (followedUser.isPresent() && follower.isPresent()) {
            Follower newFollower = new Follower(follower.get(), followedUser.get());
            followerRepository.save(newFollower);
            return "✅ Successfully followed the user.";
        } else {
            return "❌ User not found.";
        }
    }


    // Unfollow a user
    public String unfollowUser(Long followedUserId, Long followerId) {
        Optional<Follower> relationship = followerRepository.findByFollowedUserIdAndFollowerId(followedUserId, followerId);
        if (relationship.isPresent()) {
            followerRepository.delete(relationship.get());
            return "Successfully unfollowed the user.";
        }
        return "You are not following this user.";
    }

    // Get all followers of a user
    public List<Follower> getFollowers(Long userId) {
        return followerRepository.findByFollowedUserId(userId);
    }

    // Get all users a specific user is following
    public List<Follower> getFollowing(Long userId) {
        return followerRepository.findByFollowerId(userId);
    }
}
