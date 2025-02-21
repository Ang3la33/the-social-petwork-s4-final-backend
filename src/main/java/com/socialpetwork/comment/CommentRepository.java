package com.socialpetwork.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findFromPostId(Long postId);
    List<Comment> findFromUserId(Long userId);
  // CHECK THIS - outcome
    Optional<Comment> findFromId(long id);
}

