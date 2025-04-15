package com.socialpetwork.service;

import com.socialpetwork.entity.Comment;
import com.socialpetwork.entity.Post;
import com.socialpetwork.entity.User;
import com.socialpetwork.repository.CommentRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    private AutoCloseable closeable;

    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testusername");

        post = new Post();
        post.setId(1L);
        post.setContent("Test post");
        post.setUser(user);

        comment = new Comment("Test comment", user, post);
        comment.setId(1L);
        comment.setPostedAt(LocalDateTime.now());
    }

    @Test
    void findAllComments_returnsList() {
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        List<Comment> result = commentService.findAllComments();

        assertEquals(1, result.size());
        assertEquals("Test comment", result.get(0).getContent());
    }

}
