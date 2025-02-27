package com.socialpetwork.commenttest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.socialpetwork.comment.Comment;
import com.socialpetwork.comment.CommentRepository;
import com.socialpetwork.comment.CommentService;
import com.socialpetwork.post.Post;
import com.socialpetwork.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment comment1;
    private Comment comment2;

    private User user1;
    private User user2;
    private Post post1;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user2 = new User();
        post1 = new Post("Test Post",user1);
        comment1 = new Comment(1L, "First comment", user1, post1, LocalDateTime.now());
        comment2 = new Comment(2L, "Second comment", user2, post1, LocalDateTime.now());
    }

    @Test
    void testFindAllComments() {
        when(commentRepository.findAll()).thenReturn(Arrays.asList(comment1, comment2));

        List<Comment> comments = commentService.findAllComments();

        assertEquals(2, comments.size());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void testFindCommentsByUser() {
        when(commentRepository.findByUser(user1)).thenReturn(List.of(comment1)); // Match repository method

        List<Comment> comments = commentService.findCommentsByUser(user1);

        assertNotNull(comments);
        assertFalse(comments.isEmpty());
        assertEquals(1, comments.size());
        assertNotNull(comments.get(0).getContent());
        assertEquals("First comment", comments.get(0).getContent());

        verify(commentRepository, times(1)).findByUser(user1); // Match repository method
    }


    @Test
    void testFindCommentsByPost() {
        when(commentRepository.findByPost(post1)).thenReturn(Arrays.asList(comment1, comment2));

        List<Comment> comments = commentService.findCommentsByPost(post1);

        assertEquals(2, comments.size());
        verify(commentRepository, times(1)).findByPost(post1);
    }

    @Test
    void testFindCommentFromId() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));

        Comment result = commentService.findCommentById(1L);

        assertNotNull(result);
        assertEquals("First comment", result.getContent());
        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateComment() {
        when(commentRepository.save(comment1)).thenReturn(comment1);

        Comment result = commentService.createComment(comment1);

        assertNotNull(result);
        assertEquals("First comment", result.getContent());
        verify(commentRepository, times(1)).save(comment1);
    }

    @Test
    void testDeleteComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));

        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).delete(comment1);
    }

    @Test
    void testUpdateComment() {
        Comment updatedComment = new Comment(1L, "Updated comment", user1, post1, LocalDateTime.now());

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);

        Comment result = commentService.updateComment(1L, updatedComment);

        assertEquals("Updated comment", result.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testFindCommentsByUser_NotFound() {
        when(commentRepository.findByUser(user1)).thenReturn(List.of());
        List<Comment> comments = commentService.findCommentsByUser(user1);
        assertNotNull(comments);
        assertTrue(comments.isEmpty());
        assertEquals(0, comments.size());
    }

    @Test
    void testFindCommentsByPost_NotFound() {
        when(commentRepository.findByPost(post1)).thenReturn(List.of());
        List<Comment> comments = commentService.findCommentsByPost(post1);
        assertNotNull(comments);
        assertTrue(comments.isEmpty());
        assertEquals(0, comments.size());
    }

}
