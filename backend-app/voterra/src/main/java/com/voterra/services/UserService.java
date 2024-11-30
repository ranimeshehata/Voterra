package com.voterra.services;

import com.voterra.entities.User;
import com.voterra.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User signupWithGoogleOrFacebook(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            System.out.println("User already exists");
            return existingUser;
        }
        System.out.println("User does not exist");
        return userRepository.save(user);
    }

}
