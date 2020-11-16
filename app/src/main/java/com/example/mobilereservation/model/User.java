package com.example.mobilereservation.model;

import java.io.Serializable;

public class User implements Serializable {

    private String account_id;
    private String password;
    private String firstname;
    private String lastname;

    public User(String account_id, String password, String firstname, String lastname) {
        this.account_id = account_id;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

}
