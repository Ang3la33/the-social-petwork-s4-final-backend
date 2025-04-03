package com.socialpetwork.service;

import com.socialpetwork.entity.Follower;
import com.socialpetwork.entity.User;
import com.socialpetwork.repository.FollowerRepository;
import com.socialpetwork.repository.UserRepository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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
        user1 = new User();
        user1.setId(1L);

        user2 = new User();
        user2.setId(2L);

        follower = new Follower(user1, user2);
    }

    @Test
    void testFollowUser_Success() {
        when(followerRepository.existsByFollowedUserIdAndFollowerId(2L, 1L)).thenReturn(false);  // user1 follows user2
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

        assertEquals("❌ You are already following this user.", result); // need same wording
        verify(followerRepository, never()).save(any(Follower.class));
    }

    @Test
    void testFollowUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty()); // emtpy?
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

        assertEquals("❌ You are not following this user.", result);   // need exact wording
        verify(followerRepository, never()).delete(any(Follower.class));
    }

    @Test
    void testGetFollowers() {
        System.out.println("Running test: testGetFollowers");
        when(followerRepository.findByFollowedUserId(1L)).thenReturn(List.of(follower));

        List<Follower> followers = followerService.getFollowers(1L);
        System.out.println("Followers found: " + followers.size());

        assertEquals(1, followers.size());
        assertEquals(user2.getId(), followers.get(0).getFollower().getId());
    }

    @Test
    void testGetFollowing() {
        System.out.println("Running test: testGetFollowing");
        when(followerRepository.findByFollowerId(2L)).thenReturn(List.of(follower));

        List<Follower> following = followerService.getFollowing(2L);
        System.out.println("Following count: " + following.size());

        assertEquals(1, following.size());
        assertEquals(user1.getId(), following.get(0).getFollowedUser().getId());
    }
}