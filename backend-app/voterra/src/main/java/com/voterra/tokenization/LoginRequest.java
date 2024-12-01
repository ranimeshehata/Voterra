package com.voterra.tokenization;

public class LoginRequest {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setAccount(String account) {
        this.email = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
