package com.socialpetwork.service;

import com.socialpetwork.entity.Post;
import com.socialpetwork.entity.User;
import com.socialpetwork.repository.PostRepository;
import com.socialpetwork.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    private AutoCloseable closeable;
    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        post = new Post("Test post", user);
        post.setId(1L);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findPostById_found() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post result = postService.findPostById(1L);

        assertNotNull(result);
        assertEquals("Test post", result.getContent());
    }

    @Test
    void findPostById_notFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        Post result = postService.findPostById(99L);

        assertNull(result);
    }

    @Test
    void findPostsByUserId_returnsListOfPosts() {
        when(postRepository.findByUserId(1L)).thenReturn(List.of(post));

        List<Post> result = postService.findPostsByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test post", result.get(0).getContent());
    }

    @Test
    void createPost() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post newPost = new Post("New post", user);
        Post result = postService.createPost(newPost);

        assertNotNull(result);
        assertEquals("Test post", result.getContent());

    }

    @Test
    void updatePost() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Post updatedPost = new Post("Updated post", user);

        Post result = postService.updatePost(1L, updatedPost, 1L);

        assertNotNull(result);
        assertEquals("Updated post", result.getContent());
    }

}
