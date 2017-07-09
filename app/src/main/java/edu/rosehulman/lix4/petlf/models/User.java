package edu.rosehulman.lix4.petlf.models;

/**
 * Created by phillee on 7/9/2017.
 */

public class User {
    private String password;
    private String username;
    private String userId;

    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
