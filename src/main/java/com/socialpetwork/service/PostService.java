package com.socialpetwork.service;

import com.socialpetwork.entity.Post;
import com.socialpetwork.entity.User;
import com.socialpetwork.repository.PostRepository;
import com.socialpetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all posts.
     */
    public List<Post> findAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Get a post by its ID.
     */
    public Post findPostById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    /**
     * Get all posts made by a specific user.
     */
    public List<Post> findPostsByUserId(long userId) {
        return postRepository.findByUserId(userId);
    }

    /**
     * Create a new post for a given user.
     */
    public Post createPost(Post newPost) {
        if (newPost.getUser() == null || newPost.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        User user = userRepository.findById(newPost.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        newPost.setUser(user);
        newPost.setCreatedAt(LocalDateTime.now());

        return postRepository.save(newPost);
    }

    /**
     * Update an existing post.
     */
    public Post updatePost(long id, Post updatedPost, Long userId) {
        Post existingPost = findPostById(id);
        if (existingPost == null) return null;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        existingPost.setContent(updatedPost.getContent());
        existingPost.setCreatedAt(LocalDateTime.now());
        existingPost.setUser(user);

        return postRepository.save(existingPost);
    }

    /**
     * Delete a post by its ID.
     */
    public boolean deletePost(long id) {
        if (!postRepository.existsById(id)) return false;
        postRepository.deleteById(id);
        return true;
    }
}
