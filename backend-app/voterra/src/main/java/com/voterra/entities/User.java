package com.voterra.entities;

import java.util.Date;
import java.util.List;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private userType userType;
    private gender gender;
    private Date dateOfBirth;
    private List<String> friends;
    private List<String> savedPosts;            //list of (userID - postID)

    public enum userType {
        ADMIN, USER, SUPERADMIN
    }

    public enum gender {
        MALE, FEMALE
    }

    public User(String id, String username, String email, String password, String firstName, String lastName,
                Date dateOfBirth, List<String> friends, List<String> savedPosts, userType userType, gender gender) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.friends = friends;
        this.savedPosts = savedPosts;
        this.userType = userType;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public userType getUserType() {
        return userType;
    }

    public gender getGender() {
        return gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<String> getSavedPosts() {
        return savedPosts;
    }
}
