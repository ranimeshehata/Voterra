package com.voterra.services;

import com.voterra.entities.User;
import com.voterra.repos.UserRepository;
import com.voterra.tokenization.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final JwtUtils jwtUtils = new JwtUtils();

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User signup(User user) {
        Optional<User> existingUser = userRepository.findById(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with this account already exists: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
