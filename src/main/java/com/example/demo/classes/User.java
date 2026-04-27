package com.example.demo.classes;

import java.io.Serializable;

public class User implements Serializable {
    private final int uid;
    private final String username;
    private final String email;

    public User(int uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
