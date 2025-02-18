package com.socialpetwork.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post samplePost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        samplePost = new Post("Testing post!", new Timestamp(System.currentTimeMillis()), 1L);
        samplePost.setId(1L);
    }

    @Test
    void testFindPostById() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(samplePost));
        Post postFound = postService.findPostById(1L);
        assertNotNull(postFound);
        assertEquals("Testing post!", postFound.getContent());
    }

    @Test
    void testCreatePost() {
        when(postRepository.save(samplePost)).thenReturn(samplePost);
        Post createdPost = postService.createPost(samplePost);
        assertNotNull(createdPost);
        assertEquals("Testing post!", createdPost.getContent());
    }

    @Test
    void testFindPostsByUserId() {
        when(postRepository.findByUserId(1L)).thenReturn(List.of(samplePost));
        List<Post> postsFound = postService.findPostsByUserId(1L);
        assertNotNull(postsFound);
        assertEquals(1, postsFound.size());
    }

    @Test
    void testDeletePost() {
        when(postRepository.existsById(1L)).thenReturn(true);
        boolean isDeleted = postService.deletePost(1L);
        assertTrue(isDeleted);
        verify(postRepository, times(1)).deleteById(1L);
    }

}
