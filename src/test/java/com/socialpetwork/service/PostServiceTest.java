package com.socialpetwork.service;

import com.socialpetwork.entity.Post;
import com.socialpetwork.entity.User;
import com.socialpetwork.repository.PostRepository;
import com.socialpetwork.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

}
