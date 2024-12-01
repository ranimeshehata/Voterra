package com.voterra.tokenization;

import com.voterra.entities.User;

public class JwtResponse {
    private String token;
    private User user;

    public JwtResponse(Object [] token) {
        this.user = (User) token[0];
        this.token = token[1].toString();
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
