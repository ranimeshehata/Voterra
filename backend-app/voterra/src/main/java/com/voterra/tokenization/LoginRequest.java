package com.voterra.tokenization;

public class LoginRequest {

    private String account;
    private String password;

<<<<<<< HEAD
    public String getAccount() {
        return account;
=======
    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
>>>>>>> SCRUM-17-user-login
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
