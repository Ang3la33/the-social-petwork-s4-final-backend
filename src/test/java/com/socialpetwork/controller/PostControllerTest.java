package com.socialpetwork.controller;

import com.socialpetwork.entity.Post;
import com.socialpetwork.entity.User;
import com.socialpetwork.service.PostService;
import com.socialpetwork.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    private AutoCloseable closeable;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        user = new User("Test User", "test@example.com", "testuser", "password");
        user.setId(1L);
        post = new Post("Hello world", user);
        post.setId(10L);
    }

    @Test
    void getPostsByUserId_returnsList() {
        when(postService.findPostsByUserId(1L)).thenReturn(List.of(post));

        ResponseEntity<List<Post>> response = postController.getPostsByUserId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(post, response.getBody().get(0));
    }

    @Test
    void getPostById_found() {
        when(postService.findPostById(10L)).thenReturn(post);

        ResponseEntity<Post> response = postController.getPostById(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Hello world", response.getBody().getContent());
    }

    @Test
    void getPostById_notFound() {
        when(postService.findPostById(99L)).thenReturn(null);

        ResponseEntity<Post> response = postController.getPostById(99L);

        assertEquals(404, response.getStatusCodeValue());
    }
}
