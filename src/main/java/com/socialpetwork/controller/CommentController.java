package com.socialpetwork.controller;

import com.socialpetwork.dto.CommentDTO;
import com.socialpetwork.service.CommentService;
import com.socialpetwork.entity.Comment;
import com.socialpetwork.repository.PostRepository;
import com.socialpetwork.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.findAllComments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable long id) {
        try {
            Comment comment = commentService.findCommentById(id);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException error) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUserId(@PathVariable long userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(commentService.findCommentsByUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable long postId) {
        return postRepository.findById(postId)
                .map(post -> {
                    List<CommentDTO> dtos = commentService.findCommentsByPost(post).stream()
                            .map(comment -> new CommentDTO(
                                    comment.getId(),
                                    comment.getContent(),
                                    comment.getUser().getUsername(),
                                    comment.getPostedAt()
                            ))
                            .toList();
                    return ResponseEntity.ok(dtos);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        // Fetch full user
        Long userId = comment.getUser().getId();
        Long postId = comment.getPost().getId();

        var userOpt = userRepository.findById(userId);
        var postOpt = postRepository.findById(postId);

        if (userOpt.isEmpty() || postOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        comment.setUser(userOpt.get());
        comment.setPost(postOpt.get());

        Comment newComment = commentService.createComment(comment);
        return ResponseEntity.ok(newComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException error) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable long id, @RequestBody Comment comment) {
        try {
            Comment updatedComment = commentService.updateComment(id, comment);
            return ResponseEntity.ok(updatedComment);
        } catch (RuntimeException error) {
            return ResponseEntity.notFound().build();
        }
    }

}
