package com.socialpetwork.service;

import com.socialpetwork.entity.Comment;
import com.socialpetwork.entity.User;
import com.socialpetwork.entity.Post;

import com.socialpetwork.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> findAllComments(){
        return commentRepository.findAll();
    }

    public Comment findCommentById(long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No comments found"));
    }
    public List<Comment> findCommentsByUser(User user){
        return commentRepository.findByUser(user);
    }
    public List<Comment> findCommentsByPost(Post post){
        return commentRepository.findByPost(post);
    }

    public Comment createComment(Comment newComment){
        return commentRepository.save(newComment);
    }

    public void deleteComment(long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No comments found"));
        commentRepository.delete(comment);
    }

    public Comment updateComment(long id, Comment updatedComment){
        Comment comment = findCommentById(id);
        comment.setContent(updatedComment.getContent());
        comment.setPost(updatedComment.getPost());
        comment.setUser(updatedComment.getUser());
        comment.setPostedAt(updatedComment.getPostedAt());
        return commentRepository.save(comment);
    }

}
