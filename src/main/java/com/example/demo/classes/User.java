package com.example.demo.classes;

import java.io.Serializable;

public class User implements Serializable {
    private final int uid;
    private String username;
    private String firstname;
    private String lastname;

    public User(int uid, String username, String firstname, String lastname) {
        this.uid = uid;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return uid + ": " + username + " | " + firstname + " " + lastname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
