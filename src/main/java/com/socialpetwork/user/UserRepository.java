package com.socialpetwork.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    // I picked these as they are unique, but I can change or add
    // Find user by email and Check if email exist
    User findByEmail(String email);
    boolean existsByEmail(String email);

    // Find user by username and check if exist
    User findByUsername(String username);
    boolean existsByUsername(String username);

}