package com.socialpetwork.comment;

import com.socialpetwork.user.User;
import com.socialpetwork.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findByUser(User user);

    Optional<Comment> findByPostAndUser(Post post, User user);
}

