package com.socialpetwork.service;

import com.socialpetwork.entity.User;
import com.socialpetwork.entity.Post;
import com.socialpetwork.entity.UserType;
import com.socialpetwork.repository.PostRepository;
import com.socialpetwork.repository.UserRepository;
import com.socialpetwork.exception.UserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public User createNewUser(User newUser) throws UserException {
        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new UserException("This username already exists");
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new UserException("A user with this email already exists");
        }

        if (newUser.getType() == null) {
            newUser.setType(UserType.USER);
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        newUser.setAbout("empty");
        newUser.setBirthday("empty");

        return userRepository.save(newUser);
    }

    public Long getUserIdByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user.getId();
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserFromId(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserFromUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User updateUser(Long id, User updatedInfo) throws UserException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found"));

        if (!existingUser.getUsername().equals(updatedInfo.getUsername()) &&
                userRepository.existsByUsername(updatedInfo.getUsername())) {
            throw new UserException("This username already exists");
        }

        if (!existingUser.getEmail().equals(updatedInfo.getEmail()) &&
                userRepository.existsByEmail(updatedInfo.getEmail())) {
            throw new UserException("A user with this email already exists");
        }

        existingUser.setName(updatedInfo.getName());
        existingUser.setEmail(updatedInfo.getEmail());
        existingUser.setUsername(updatedInfo.getUsername());
        existingUser.setAbout(updatedInfo.getAbout());
        existingUser.setBirthday(updatedInfo.getBirthday());

        // Only update password if it's not null/blank and different
        if (updatedInfo.getPassword() != null && !updatedInfo.getPassword().isBlank()) {
            if (!passwordEncoder.matches(updatedInfo.getPassword(), existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(updatedInfo.getPassword()));
            }
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }
}
