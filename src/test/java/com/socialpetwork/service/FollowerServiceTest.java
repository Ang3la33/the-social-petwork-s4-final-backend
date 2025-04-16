package com.socialpetwork.service;

import com.socialpetwork.entity.Follower;
import com.socialpetwork.entity.User;
import com.socialpetwork.repository.FollowerRepository;
import com.socialpetwork.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FollowerServiceTest {

    @InjectMocks
    private FollowerService followerService;

    @Mock
    private FollowerRepository followerRepository;

    @Mock
    private UserRepository userRepository;

    private AutoCloseable closeable;

    private User user1, user2;
    private Follower follower;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setId(1L);
        user1.setUsername("username1");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("username2");

        follower = new Follower(user2, user1);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
