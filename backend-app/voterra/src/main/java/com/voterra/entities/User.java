package com.voterra.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Document(collection = "users")
public class User {
    @Id
    private String email;
    private String password;
    private String username;
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

    public User(String email, String password, String username, String firstName, String lastName,
                Date dateOfBirth, List<String> friends, List<String> savedPosts, userType userType, gender gender) {
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserType(User.userType userType) {
        this.userType = userType;
    }

    public void setGender(User.gender gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public void setSavedPosts(List<String> savedPosts) {
        this.savedPosts = savedPosts;
    }
}
