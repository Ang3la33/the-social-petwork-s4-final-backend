package com.socialpetwork.post;

import com.socialpetwork.user.User;
import com.socialpetwork.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Post createPost(Post newPost, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        newPost.setUserId(user);
        return postRepository.save(newPost);
    }

    public Post updatePost(long id, Post updatedPost, Long userId) {
        Post postToUpdate = findPostById(id);

        if (postToUpdate != null) {
            postToUpdate.setContent(updatedPost.getContent());
            postToUpdate.setCreatedAt(updatedPost.getCreatedAt());

            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            postToUpdate.setUserId(user);

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
