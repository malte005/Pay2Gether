package com.maltedammann.pay2gether.pay2gether.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by damma on 26.10.2016.
 */

@IgnoreExtraProperties
public class User {
    private String id;
    private String name;
    private String mail;
    private List<Bill> bills;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", bills=" + bills +
                '}';
    }
}

