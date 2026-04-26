package com.example.volunthero;

public class User {
    public String uid;
    public String username;
    public String email;
    public String role;
    
    public String age;
    public String interests;
    public String skills;
    
    public String orgName;
    public String orgType;
    public String bio;

    public User() {
    }

    public User(String uid, String username, String email, String role) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
