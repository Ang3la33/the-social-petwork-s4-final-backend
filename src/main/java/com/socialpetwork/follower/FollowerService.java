// Just a function to follow and unfollow. Can not guaranty that they work yet.
// When it will be time to actually check I will make adjustments for them to work.
// I commented the whole part until the User part will be ready. I am not sure that the functions communicate with the
// User files correctly. All of this will be checked and I will take care for all the troubles it causes.
// If anything needed to be corrected in my files, just reach out to me, I will correct my files.

// I am sure that this is not a full class, but later, when we will work on the functionality on the whole logic of the
// website I will make sure that the following part works correctly, and it not messes up the User parts.

// No testing for this file for now.







//package com.socialpetwork.follower;

//import com.socialpetwork.user.User;
//import com.socialpetwork.user.UserRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class FollowerService {
//
//    private final FollowerRepository followerRepository;
//    private final UserRepository userRepository;
//
//    public FollowerService(FollowerRepository followerRepository, UserRepository userRepository) {
//        this.followerRepository = followerRepository;
//        this.userRepository = userRepository;
//    }
//
//    // Follow a user
//    public String followUser(Long followedUserId, Long followerId) {
//        if (followerRepository.existsByFollowedUserIdAndFollowerId(followedUserId, followerId)) {
//            return "You are already following this user.";
//        }
//
//        Optional<User> followedUser = userRepository.findById(followedUserId);
//        Optional<User> follower = userRepository.findById(followerId);
//
//        if (followedUser.isPresent() && follower.isPresent()) {
//            Follower newFollower = new Follower(followedUser.get(), follower.get());
//            followerRepository.save(newFollower);
//            return "Successfully followed the user.";
//        } else {
//            return "User not found.";
//        }
//    }
//
//    // Unfollow a user
//    public String unfollowUser(Long followedUserId, Long followerId) {
//        List<Follower> relationships = followerRepository.findByFollowedUserId(followedUserId);
//        for (Follower relationship : relationships) {
//            if (relationship.getFollower().getId().equals(followerId)) {
//                followerRepository.delete(relationship);
//                return "Successfully unfollowed the user.";
//            }
//        }
//        return "You are not following this user.";
//    }
//
//    // Get all followers of a user
//    public List<Follower> getFollowers(Long userId) {
//        return followerRepository.findByFollowedUserId(userId);
//    }
//}

