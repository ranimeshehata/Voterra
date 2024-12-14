package com.voterra.services;
import com.voterra.entities.SuperAdmin;
import com.voterra.entities.User;
import com.voterra.repos.UserRepository;
import com.voterra.tokenization.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.voterra.entities.User.userType.SUPERADMIN;

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
        Optional<User> existingUsername = userRepository.findByUsername(user.getUsername());
        if (existingUsername.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if(user.getUserType()==SUPERADMIN){
            SuperAdmin superAdmin = new SuperAdmin(user.getEmail(), user.getPassword(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getFriends(), user.getSavedPosts(), user.getUserType(), user.getGender());
            superAdmin.setPassword(passwordEncoder.encode(superAdmin.getPassword()));
            userRepository.save(superAdmin);
            return new Object[] {superAdmin, jwtUtils.generateToken(superAdmin.getEmail())};
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new Object[] {user, jwtUtils.generateToken(user.getEmail())};
    }

    public Object[] signupOrLoginWithGoogleOrFacebook(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return new Object[] {existingUser, jwtUtils.generateToken(user.getEmail())};
        }
        return new Object[] {userRepository.save(user), jwtUtils.generateToken(user.getEmail())};
    }

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
        SecurityContextHolder.clearContext();
    }
    public static boolean isAdmin(User user) {
        return user.getUserType() == User.userType.ADMIN ;
    }
}
