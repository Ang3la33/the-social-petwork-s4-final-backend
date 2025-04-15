package com.socialpetwork.controller;

import com.socialpetwork.entity.Follower;
import com.socialpetwork.entity.User;
import com.socialpetwork.service.FollowerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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

    @Test
    void followUser_success() {
        when(followerService.followUser(1L,2L)).thenReturn("✅ Successfully followed bob");

        ResponseEntity<String> response = followerController.followUser(1L,2L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("✅ Successfully followed bob", response.getBody());
    }

}
