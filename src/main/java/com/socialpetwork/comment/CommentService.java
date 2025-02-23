package com.socialpetwork.comment;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> findAllComments(){
        return (List<Comment>) commentRepository.findAll();
    }

    public List<Comment> findCommentsFromUserId(long userId) {
        return commentRepository.findFromUserId(userId);
    }

    public List<Comment> findCommentsFromPostId(long postId) {
        return commentRepository.findFromPostId(postId);
    }

    public Comment findCommentFromId(long id) {
        return commentRepository.findFromId(id)
                .orElseThrow(() -> new RuntimeException("No comment was found with the id"));
    }

    public Comment createComment(Comment newComment){
        return commentRepository.save(newComment);
    }

    public void deleteComment(long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No comment found with the id."));
        commentRepository.delete(comment);
    }

    public Comment updateComment(long id, Comment updatedComment) {
        return commentRepository.findById(id)
                .map(currentComment -> {
                    currentComment.setContent(updatedComment.getContent());
                    currentComment.setPostedAt(updatedComment.getPostedAt());
                    currentComment.setUserId(updatedComment.getUserId());
                    currentComment.setPostId(updatedComment.getPostId());
                    return commentRepository.save(currentComment);
                })
                .orElseThrow(() -> new RuntimeException("No comment was found with the id"));
    }


}
