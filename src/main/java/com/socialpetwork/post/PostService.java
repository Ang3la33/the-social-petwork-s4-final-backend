package com.socialpetwork.post;

import com.socialpetwork.user.User;
import com.socialpetwork.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Post> findAllPosts() {
        return (List<Post>) postRepository.findAll();
    }

    public Post findPostById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> findPostsByUserId(long userId) {
        return postRepository.findByUserId(userId);
    }

    public Post createPost(Post newPost) {
        if (newPost.getUser() == null || newPost.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        User user = userRepository.findById(newPost.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        newPost.setUser(user);
        return postRepository.save(newPost);
    }


    public Post updatePost(long id, Post updatedPost, Long userId) {
        Post postToUpdate = findPostById(id);

        if (postToUpdate != null) {
            postToUpdate.setContent(updatedPost.getContent());
            postToUpdate.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime());

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            postToUpdate.setUser(user);
            return postRepository.save(postToUpdate);
        }

        return null;
    }

    public boolean deletePost(long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

