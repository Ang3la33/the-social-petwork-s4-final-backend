package com.socialpetwork.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create New User
    public User createNewUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserException("This email is already in use. Please try a different one.");
        }
        if(userRepository.existsByUsername(user.getUsername())){
            throw new UserException("This username already exists. Please try a different one.");
        }
        return userRepository.save(user);
    }

    // Delete User by ID
    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserException("No user found with the id.");
        }
        userRepository.delete(user);
        return true;
    }

    //  Get User by ID
    public User getUserFromId(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            throw new UserException("No user was found with the id.");
        }
        return user;
    }

    // Get User by Username
    public User getUserFromUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UserException("No user found with the username.");
        }
        return user;
    }

    //  Update User Information
    public User updateUser(Long id, User userInfo){
        User user = getUserFromId(id);
        user.setName(userInfo.getName());
        user.setBirthday(userInfo.getBirthday());
        user.setEmail(userInfo.getEmail());
        user.setProfileUrl(userInfo.getProfileUrl());

        return userRepository.save(user);
    }

    // Get All Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get User ID by Username (For Login)
    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserException("No user found with the username.");
        }
        return user.getId();
    }
}


