package com.socialpetwork.follower;

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

    /**
     * Endpoint to follow a user.
     * @param id The ID of the user who is following.
     * @param targetId The ID of the user to be followed.
     * @return Response message indicating the follow status.
     */
    @PostMapping("/{id}/follow/{targetId}")
    public ResponseEntity<String> followUser(@PathVariable Long id, @PathVariable Long targetId) {
        String response = followerService.followUser(id, targetId);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to get the list of followers of a user.
     * @param id The ID of the user whose followers are being retrieved.
     * @return List of followers.
     */
    @GetMapping("/{id}/followers")
    public ResponseEntity<List<Follower>> getFollowers(@PathVariable Long id) {
        List<Follower> followers = followerService.getFollowers(id);
        return ResponseEntity.ok(followers);
    }

    /**
     * Endpoint to get the list of users a specific user is following.
     * @param id The ID of the user whose following list is being retrieved.
     * @return List of users being followed.
     */
    @GetMapping("/{id}/following")
    public ResponseEntity<List<Follower>> getFollowing(@PathVariable Long id) {
        List<Follower> following = followerService.getFollowing(id);
        return ResponseEntity.ok(following);
    }
}
