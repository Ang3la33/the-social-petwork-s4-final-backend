package com.socialpetwork.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// User can be found by email and username
// each should be assigned an ID
// User need to change name, birthday, email
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

    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserException("No user found with the id.");
        }
        userRepository.delete(user);
        return true;
    }

    public User getUserFromId(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            throw new UserException("No user was found with the id.");
        }
        return user;
    }

    public User getUserFromUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UserException("No user found with the username.");
        }
        return user;
    }

    public User updateUser(Long id, User userInfo){
        User user = getUserFromId(id);
        user.setName(userInfo.getName());
        user.setBirthday(userInfo.getBirthday());
        user.setEmail(userInfo.getEmail());
        user.setProfileUrl(userInfo.getProfileUrl());

        return userRepository.save(user);
    }

    // Added
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

