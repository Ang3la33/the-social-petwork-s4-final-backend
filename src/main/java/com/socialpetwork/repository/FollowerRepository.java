package com.socialpetwork.repository;

import com.socialpetwork.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {

    // Get all followers of a user
    List<Follower> findByFollowedUserId(Long followedUserId);

    // Get all users a specific user is following
    List<Follower> findByFollowerId(Long followerId);

    // Check if a follow relationship exists
    boolean existsByFollowedUserIdAndFollowerId(Long followedUserId, Long followerId);

    Optional<Follower> findByFollowedUserIdAndFollowerId(Long followedUserId, Long followerId);
}

