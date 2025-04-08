package com.socialpetwork.service;

import com.socialpetwork.entity.Follower;
import com.socialpetwork.entity.User;
import com.socialpetwork.entity.UserType;
import com.socialpetwork.repository.FollowerRepository;
import com.socialpetwork.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowerServiceTest {

    @Mock
    private FollowerRepository followerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowerService followerService;

    private User user1;
    private User user2;
    private Follower follower;

    @BeforeEach
    void setUp() {
        String name = "Test User";
        String birthday = "2000-01-01";
        String email = "user@example.com";
        String username1 = "user1";
        String username2 = "user2";
        String password = "password123";
        UserType type = UserType.USER;

        user1 = new User(name, birthday, email, username1, password, type);
        user1.setId(1L);

        user2 = new User(name, birthday, email, username2, password, type);
        user2.setId(2L);

        follower = new Follower(user1, user2);
    }

    @Test
    void testFollowUser_Success() {
        when(followerRepository.existsByFollowedUserIdAndFollowerId(2L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        String result = followerService.followUser(1L, 2L);

        assertEquals("✅ Successfully followed " + user2.getUsername(), result);
        verify(followerRepository, times(1)).save(any(Follower.class));
    }

    @Test
    void testFollowUser_AlreadyFollowing() {
        when(followerRepository.existsByFollowedUserIdAndFollowerId(2L, 1L)).thenReturn(true);

        String result = followerService.followUser(1L, 2L);

        assertEquals("❌ You are already following this user.", result);
        verify(followerRepository, never()).save(any(Follower.class));
    }

    @Test
    void testFollowUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        String result = followerService.followUser(1L, 2L);

        assertEquals("❌ User not found.", result);
        verify(followerRepository, never()).save(any(Follower.class));
    }

    @Test
    void testUnfollowUser_Success() {
        when(followerRepository.findByFollowedUserIdAndFollowerId(2L, 1L)).thenReturn(Optional.of(follower));

        String result = followerService.unfollowUser(2L, 1L);

        assertEquals("✅ Successfully unfollowed " + user2.getUsername(), result);
        verify(followerRepository, times(1)).delete(follower);
    }

    @Test
    void testUnfollowUser_NotFollowing() {
        when(followerRepository.findByFollowedUserIdAndFollowerId(2L, 1L)).thenReturn(Optional.empty());

        String result = followerService.unfollowUser(2L, 1L);

        assertEquals("❌ You are not following this user.", result);
        verify(followerRepository, never()).delete(any(Follower.class));
    }

    @Test
    void testGetFollowers() {
        when(followerRepository.findByFollowedUserId(1L)).thenReturn(List.of(follower));

        List<Follower> followers = followerService.getFollowers(1L);

        assertEquals(1, followers.size());
        assertEquals(user2.getId(), followers.get(0).getFollower().getId());
    }

    @Test
    void testGetFollowing() {
        when(followerRepository.findByFollowerId(2L)).thenReturn(List.of(follower));

        List<Follower> following = followerService.getFollowing(2L);

        assertEquals(1, following.size());
        assertEquals(user1.getId(), following.get(0).getFollowedUser().getId());
    }
}
