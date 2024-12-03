package com.voterra.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
public class Admin extends User {

    public Admin(String email, String password, String username,  String firstName, String lastName, Date dateOfBirth, List<String> friends, List<String> savedPosts, User.userType userType, User.gender gender) {
        super(email, password, username, firstName, lastName, dateOfBirth, friends, savedPosts, userType, gender);
    }

    public void deletePost(String postId) {
        // Logic to delete a post
    }

    public void leavePost(String postId) {
        // Logic to leave a post
    }
}
