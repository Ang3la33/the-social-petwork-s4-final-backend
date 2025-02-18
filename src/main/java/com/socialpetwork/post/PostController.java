package com.socialpetwork.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postService.findAllPosts();
    }

    @GetMapping("/post/{id}")
    public Post getPostByID(@PathVariable long id) {
        return postService.findPostById(id);
    }

    @GetMapping("/posts/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable long userId) {
        return postService.findPostsByUserId(userId);
    }

    @PostMapping("/post")
    public Post createPost(@RequestBody Post newPost) {
        return postService.createPost(newPost);
    }

    @PutMapping("/post/{id}")
    public Post updatePost(@PathVariable long id, @RequestBody Post updatedPost) {
        return postService.updatePost(id, updatedPost);
    }

    @DeleteMapping("/post/{id}")
    public String deletePost(@PathVariable long id) {
        boolean isDeleted = postService.deletePost(id);
        return isDeleted ? "Post successfully deleted" : "Post not found";
    }
}
