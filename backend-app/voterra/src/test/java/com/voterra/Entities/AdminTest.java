package com.voterra.entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdminTest {

    @Test
    void testAdminConstructor() {
        // Arrange
        String email = "admin@example.com";
        String password = "password123";
        String username = "adminUser";
        String firstName = "Admin";
        String lastName = "User";
        Date dateOfBirth = new Date();
        List<String> friends = new ArrayList<>();
        List<String> savedPosts = new ArrayList<>();
        User.userType userType = User.userType.ADMIN;
        User.gender gender = User.gender.MALE;

        // Act
        Admin admin = new Admin(email, password, username, firstName, lastName, dateOfBirth, friends, savedPosts, userType, gender);

        // Assert
        assertEquals(email, admin.getEmail());
        assertEquals(password, admin.getPassword());
        assertEquals(username, admin.getUsername());
        assertEquals(firstName, admin.getFirstName());
        assertEquals(lastName, admin.getLastName());
        assertEquals(dateOfBirth, admin.getDateOfBirth());
        assertEquals(friends, admin.getFriends());
        assertEquals(savedPosts, admin.getSavedPosts());
        assertEquals(userType, admin.getUserType());
        assertEquals(gender, admin.getGender());
    }

    @Test
    void testDeletePost() {
        Admin admin = new Admin("admin@example.com", "password123", "adminUser", "Admin", "User", new Date(), new ArrayList<>(), new ArrayList<>(), User.userType.ADMIN, User.gender.MALE);
        String postId = "post123";

        admin.deletePost(postId);

    }

    @Test
    void testLeavePost() {
        Admin admin = new Admin("admin@example.com", "password123", "adminUser", "Admin", "User", new Date(), new ArrayList<>(), new ArrayList<>(), User.userType.ADMIN, User.gender.MALE);
        String postId = "post123";

        admin.leavePost(postId);

    }
}

