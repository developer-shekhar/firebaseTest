package com.google.firebase.codelab.friendlychat;

/**
 * Created by mac3 on 22/12/16.
 */

public class AppUser {

    private String name;
    private String email;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public AppUser() {
    }

    public AppUser(String name, String email) {
        this.name = name;
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
