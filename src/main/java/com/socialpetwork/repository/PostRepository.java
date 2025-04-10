package com.socialpetwork.repository;

import com.socialpetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Find all posts created by a specific user.
     */
    List<Post> findByUserId(Long userId);
    List<Post> findAllByOrderByCreatedAtDesc();
}
