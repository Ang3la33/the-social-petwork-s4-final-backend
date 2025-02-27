package com.socialpetwork.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable long id) {
        Post post = postService.findPostById(id);
        return post != null ?
                new ResponseEntity<>(post, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable long userId) {
        return postService.findPostsByUserId(userId);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> createPost(@RequestBody Post post, @RequestParam long userId) {
        Post createdPost = postService.createPost(post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable long id, @RequestBody Post post, @RequestParam long userId) {
        Post updatedPost = postService.updatePost(id, post, userId);
        return updatedPost != null ?
                new ResponseEntity<>(updatedPost, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable long id) {
        boolean isDeleted = postService.deletePost(id);
        return isDeleted ? "Post successfully deleted" : "Post not found";
    }
}
