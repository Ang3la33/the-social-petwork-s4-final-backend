package com.socialpetwork.controller;

import com.socialpetwork.entity.Follower;
import com.socialpetwork.entity.User;
import com.socialpetwork.service.FollowerService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FollowerControllerTest {

    @InjectMocks
    private FollowerController followerController;

    @Mock
    private FollowerService followerService;

    private AutoCloseable closeable;
    private User user1, user2;
    private Follower follower;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        user1 = new User("Alice", "alice@mail.com", "alice", "pass");
        user1.setId(1L);
        user2 = new User("Bob", "bob@mail.com", "bob", "pass");
        user2.setId(2L);

        follower = new Follower(user2, user1);
    }

}
