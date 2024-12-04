package com.voterra;

import com.voterra.entities.User;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        User user = new User();
        String email = "test@example.com";
        String password = "password123";
        String username = "testuser";
        String firstName = "John";
        String lastName = "Doe";
        Date dateOfBirth = new Date();
        List<String> friends = List.of("friend1", "friend2");
        List<String> savedPosts = List.of("post1", "post2");
        User.userType userType = User.userType.USER;
        User.gender gender = User.gender.MALE;

        // Act
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setFriends(friends);
        user.setSavedPosts(savedPosts);
        user.setUserType(userType);
        user.setGender(gender);

        // Assert
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(username, user.getUsername());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(dateOfBirth, user.getDateOfBirth());
        assertEquals(friends, user.getFriends());
        assertEquals(savedPosts, user.getSavedPosts());
        assertEquals(userType, user.getUserType());
        assertEquals(gender, user.getGender());
    }
}