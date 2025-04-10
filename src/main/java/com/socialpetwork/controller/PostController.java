package com.socialpetwork.controller;

import com.socialpetwork.entity.Post;
import com.socialpetwork.entity.User;
import com.socialpetwork.service.PostService;
import com.socialpetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;


import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:3000") // Adjust to match frontend port
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    /**
     * Get all posts
     */
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.findAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Get all posts by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable long userId) {
        List<Post> posts = postService.findPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    /**
     * Get a specific post by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable long id) {
        Post post = postService.findPostById(id);
        return (post != null) ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

    /**
     * Create a new post for a user
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam long userId) {

        User user = userService.getUserFromId(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            try {
                String originalName = image.getOriginalFilename();
                if (originalName == null || originalName.trim().isEmpty()) {
                    originalName = "image.jpg";
                }
                originalName = originalName.replaceAll("\\s+", "_");
                String fileName = System.currentTimeMillis() + "_" + originalName;

                System.out.println("📷 Uploading image: " + originalName);
                System.out.println("📦 Size: " + image.getSize() + " bytes");

                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                File destination = new File(uploadDir + fileName);
                image.transferTo(destination);
                System.out.println("✅ Image saved to: " + destination.getAbsolutePath());

                imageUrl = "/uploads/" + fileName;
            } catch (IOException e) {
                System.out.println("❌ Image saving failed: " + e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        Post post = new Post(content, user);
        post.setImageUrl(imageUrl);

        Post createdPost = postService.createPost(post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    /**
     * Update an existing post
     */
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable long id, @RequestBody Post post, @RequestParam long userId) {
        User user = userService.getUserFromId(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        post.setUser(user);
        Post updatedPost = postService.updatePost(id, post, userId);
        return (updatedPost != null) ?
                ResponseEntity.ok(updatedPost) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Delete a post
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable long id) {
        boolean isDeleted = postService.deletePost(id);
        return isDeleted ?
                ResponseEntity.ok("Post successfully deleted") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
    }
}
