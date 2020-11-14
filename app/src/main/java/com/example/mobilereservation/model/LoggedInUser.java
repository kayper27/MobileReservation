package com.example.mobilereservation.model;

public class LoggedInUser {

    private String account_id;
    private String account_type;
    private String status;

    public LoggedInUser(String account_id, String account_type) {
        this.account_id = account_id;
        this.account_type = account_type;
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

    public void setUserStatus(String status) {
        this.status = status;
    }
}