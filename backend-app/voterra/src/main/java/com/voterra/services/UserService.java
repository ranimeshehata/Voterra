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

    public Object[] signup(User user) {
        Optional<User> existingUser = userRepository.findById(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }
<<<<<<< HEAD
=======
        Optional<User> existingUsername = userRepository.findByUsername(user.getUsername());
        if (existingUsername.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
>>>>>>> SCRUM-17-user-login

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new Object[] {user, jwtUtils.generateToken(user.getEmail())};
    }
    
      public Object[] signupOrLoginWithGoogleOrFacebook(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            System.out.println("User already exists");
            return new Object[] {existingUser, jwtUtils.generateToken(user.getEmail())};
        }
        System.out.println("User does not exist");
        return new Object[] {userRepository.save(user), jwtUtils.generateToken(user.getEmail())};
    }
<<<<<<< HEAD
=======

    public Object[] login(String email, String password) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return new Object[] {user, jwtUtils.generateToken(email)};
    }

    public boolean forgetPassword(String email , String newPassword) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("User not found with account: " + email));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    public void signOut() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Signing out: " + email);
        SecurityContextHolder.clearContext();
    }

>>>>>>> SCRUM-17-user-login
}
