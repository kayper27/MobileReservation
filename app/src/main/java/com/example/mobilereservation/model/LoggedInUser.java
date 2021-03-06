package com.example.mobilereservation.model;

import java.io.Serializable;

public class LoggedInUser implements Serializable {

    private String account_id;
    private String account_type;
    private String status;
    private String token;
    private String firstname;
    private String lastname;
    private String details;

    public LoggedInUser(String account_id, String account_type, String status, String firstname, String lastname) {
        this.account_id = account_id;
        this.account_type = account_type;
        this.status = status;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getAccount_id() {
        return account_id;
    }

    public String getAccount_type() {
        return account_type;
    }

    public String getUserStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String detail) {
        this.details = detail;
    }
}