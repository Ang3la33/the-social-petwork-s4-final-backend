package com.socialpetwork.controller;

import com.socialpetwork.entity.Follower;
import com.socialpetwork.service.FollowerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class FollowerController {

    private final FollowerService followerService;

    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @PostMapping("/{id}/follow/{targetId}")
    public ResponseEntity<String> followUser(@PathVariable Long id, @PathVariable Long targetId) {
        String response = followerService.followUser(id, targetId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/unfollow/{targetId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long id, @PathVariable Long targetId) {
        String response = followerService.unfollowUser(targetId, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<Follower>> getFollowers(@PathVariable Long id) {
        List<Follower> followers = followerService.getFollowers(id);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<List<Follower>> getFollowing(@PathVariable Long id) {
        List<Follower> following = followerService.getFollowing(id);
        return ResponseEntity.ok(following);
    }
}

