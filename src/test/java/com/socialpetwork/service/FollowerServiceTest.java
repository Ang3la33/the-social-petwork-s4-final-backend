package com.socialpetwork.service;

import com.socialpetwork.entity.Follower;
import com.socialpetwork.entity.User;
import com.socialpetwork.repository.FollowerRepository;
import com.socialpetwork.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void followUser_success() {
        when(followerRepository.existsByFollowedUserIdAndFollowerId(2L, 1L)).thenReturn(false);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        String result = followerService.followUser(1L, 2L);

        assertEquals("✅ Successfully followed username2", result);
        verify(followerRepository).save(any(Follower.class));
    }

    @Test
    void followUser_sameId_fail() {
        String result = followerService.followUser(1L, 1L);

        assertEquals("❌ You can't follow yourself.", result);
    }

    @Test
    void followUser_alreadyFollowing_fail() {
        when(followerRepository.existsByFollowedUserIdAndFollowerId(2L, 1L)).thenReturn(true);

        String result = followerService.followUser(1L, 2L);

        assertEquals("❌ You are already following this user.", result);
    }

    @Test
    void followUser_userNotFound_fail() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        String result = followerService.followUser(1L, 2L);
        assertEquals("❌ User not found.", result);
    }

    @Test
    void unfollowUser_success() {
        when(followerRepository.findByFollowedUserIdAndFollowerId(2L, 1L))
                .thenReturn(Optional.of(follower));

        String result = followerService.unfollowUser(2L, 1L);

        assertEquals("✅ Successfully unfollowed username2", result);
        verify(followerRepository).delete(follower);
    }

    @Test
    void unfollowUser_notFollowing_returnsError() {
        when(followerRepository.findByFollowedUserIdAndFollowerId(2L, 1L))
                .thenReturn(Optional.empty());

        String result = followerService.unfollowUser(2L, 1L);

        assertEquals("❌ You are not following this user.", result);
    }

    @Test
    void getFollowers_success() {
        when(followerRepository.findByFollowedUserId(2L)).thenReturn(List.of(follower));

        List<Follower> followers = followerService.getFollowers(2L);

        assertEquals(1, followers.size());
        assertEquals(user1, followers.get(0).getFollower());

    }

}