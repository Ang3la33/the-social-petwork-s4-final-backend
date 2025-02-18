package com.socialpetwork.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> findAllPosts() {
        return (List<Post>) postRepository.findAll();
    }

    public Post findPostById(long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    public List<Post> findPostsByUserId(long userId) {
        return (List<Post>) postRepository.findByUserId(userId);
    }

    public Post createPost(Post newPost) {
        return postRepository.save(newPost);
    }

    public Post updatePost(long id, Post updatedPost) {
        Post postToUpdate = findPostById(id);

        if (postToUpdate != null) {
            postToUpdate.setContent(updatedPost.getContent());
            postToUpdate.setCreatedAt(updatedPost.getCreatedAt());
            postToUpdate.setUserId(updatedPost.getUserId());

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
