package com.socialpetwork.follower;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {

    // Get all followers of a user
    List<Follower> findByFollowedUserId(Long followedUserId);

    // Get all users a specific user is following
    List<Follower> findByFollowerId(Long followerId);

    // Check if a follower relationship exists
    boolean existsByFollowedUserIdAndFollowerId(Long followedUserId, Long followerId);
}

