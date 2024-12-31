package com.voterra.Entities;

import com.voterra.entities.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
    @Test
    void testToString() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String username = "testuser";
        String firstName = "Test";
        String lastName = "User";
        User.userType userType = User.userType.USER;
        User.gender gender = User.gender.MALE;
        Date dateOfBirth = new Date(946684800000L); // Jan 1, 2000
        List<String> friends = Arrays.asList("friend1", "friend2");
        List<String> savedPosts = Arrays.asList("post1", "post2");
        List<String> reportedPosts = Arrays.asList("post3", "post4");

        // Create the User object
        User user = new User(email, password, username, firstName, lastName, dateOfBirth, friends, savedPosts, userType, gender, new ArrayList<>());

        // Expected string representation of the User object
        String expectedString = "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userType=" + userType +
                ", gender=" + gender +
                ", dateOfBirth=" + dateOfBirth +
                ", friends=" + friends +
                ", savedPosts=" + savedPosts +
                '}';

        // Act: Call toString method
        String actualString = user.toString();

        // Assert: Verify that the actual string matches the expected string
        assertEquals(expectedString, actualString);
    }
}