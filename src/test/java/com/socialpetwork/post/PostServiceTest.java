package com.socialpetwork.post;

import com.socialpetwork.user.User;
import com.socialpetwork.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    private Post samplePost;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create Mock User
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("testUser");

        // Create Mock Post by Mock User
        samplePost = new Post("Testing post!", sampleUser);
        samplePost.setId(1L);
        samplePost.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
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
       when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
       when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
           Post post = invocation.getArgument(0);
           post.setId(1L);
           return post;
       });

       Post createdPost = postService.createPost(samplePost, 1L);

       assertNotNull(createdPost);
       assertEquals("Testing post!", createdPost.getContent());
       assertEquals(sampleUser, createdPost.getUser());
    }

    @Test
    void testFindPostsByUserId() {
        when(postRepository.findByUser_Id(1L)).thenReturn(List.of(samplePost));
        List<Post> postsFound = postService.findPostsByUserId(1L);
        assertNotNull(postsFound);
        assertEquals(1, postsFound.size());
        assertEquals(sampleUser, postsFound.get(0).getUser());
    }

    @Test
    void testDeletePost() {
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(true);
        boolean isDeleted = postService.deletePost(postId);
        assertTrue(isDeleted);
        verify(postRepository, times(1)).deleteById(postId);
    }

}
