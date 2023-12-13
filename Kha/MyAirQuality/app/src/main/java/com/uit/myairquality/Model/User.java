package com.uit.myairquality.Model;

import java.io.Serializable;

public class User implements Serializable {
    public String username, password, email, token, id;
    public User () {
        this.username = "user";
        this.password = "123";
        this.email = "";
        this.token = "";
        this.id = "";
    }
    public User (String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
