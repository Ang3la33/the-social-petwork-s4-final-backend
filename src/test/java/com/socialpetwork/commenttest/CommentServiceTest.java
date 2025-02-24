package com.socialpetwork.commenttest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.socialpetwork.comment.Comment;
import com.socialpetwork.comment.CommentRepository;
import com.socialpetwork.comment.CommentService;
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

    @BeforeEach
    void setUp() {
        comment1 = new Comment(1L, "First comment", 1L, 1L, LocalDateTime.now());
        comment2 = new Comment(2L, "Second comment", 2L, 1L, LocalDateTime.now());
    }

    @Test
    void testFindAllComments() {
        when(commentRepository.findAll()).thenReturn(Arrays.asList(comment1, comment2));

        List<Comment> comments = commentService.findAllComments();

        assertEquals(2, comments.size());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void testFindCommentsFromUserId() {
        when(commentRepository.findFromUserId(1L)).thenReturn(List.of(comment1)); // Match repository method

        List<Comment> comments = commentService.findCommentsFromUserId(1L);

        assertNotNull(comments); // Ensure list is not null
        assertFalse(comments.isEmpty()); // Ensure list is not empty
        assertEquals(1, comments.size());
        assertNotNull(comments.get(0).getContent()); // Ensure content is not null
        assertEquals("First comment", comments.get(0).getContent());

        verify(commentRepository, times(1)).findFromUserId(1L); // Match repository method
    }


    @Test
    void testFindCommentsFromPostId() {
        when(commentRepository.findFromPostId(1L)).thenReturn(Arrays.asList(comment1, comment2));

        List<Comment> comments = commentService.findCommentsFromPostId(1L);

        assertEquals(2, comments.size());
        verify(commentRepository, times(1)).findFromPostId(1L);
    }

    @Test
    void testFindCommentFromId() {
        when(commentRepository.findFromId(1L)).thenReturn(Optional.of(comment1));

        Comment result = commentService.findCommentFromId(1L);

        assertNotNull(result);
        assertEquals("First comment", result.getContent());
        verify(commentRepository, times(1)).findFromId(1L);
    }

    @Test
    void testFindCommentFromId_NotFound() {
        when(commentRepository.findFromId(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> commentService.findCommentFromId(99L));

        assertTrue(exception.getMessage().contains("No comment was found with the id"));
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
    void testDeleteComment_NotFound() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> commentService.deleteComment(99L));

        assertTrue(exception.getMessage().contains("No comment found with the id."));
    }

    @Test
    void testUpdateComment() {
        Comment updatedComment = new Comment(1L, "Updated comment", 1L, 1L, LocalDateTime.now());

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);

        Comment result = commentService.updateComment(1L, updatedComment);

        assertEquals("Updated comment", result.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testUpdateComment_NotFound() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> commentService.updateComment(99L, comment1));

        assertTrue(exception.getMessage().contains("No comment was found with the id"));
    }
}
