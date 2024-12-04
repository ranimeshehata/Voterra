package com.voterra.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
public class SuperAdmin extends User {
    private List<String> listOfAdmins;

    public SuperAdmin(String email, String password, String username, String firstName, String lastName, Date dateOfBirth, List<String> friends, List<String> savedPosts, User.userType userType, User.gender gender) {
        super(email, password, username, firstName, lastName, dateOfBirth, friends, savedPosts, userType, gender);
    }

    public void makeAdmin(String userId) {
        if (!listOfAdmins.contains(userId)) {
            listOfAdmins.add(userId);
        }
    }

    public void removeAdmin(String adminId) {
        listOfAdmins.remove(adminId);
    }

    public List<String> getListOfAdmins() {
        return listOfAdmins;
    }

    public void setListOfAdmins(List<String> listOfAdmins) {
        this.listOfAdmins = listOfAdmins;
    }
}