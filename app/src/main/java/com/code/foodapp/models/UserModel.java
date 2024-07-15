package com.code.foodapp.models;

public class UserModel {
    private int id;
    private String fullname;
    private String email;
    private String address;
    private String phone;
    private boolean isAdmin;
    private String createdAt;
    private String updatedAt;

    // Constructor
    public UserModel(int id, String fullname, String email, String address, String phone, boolean isAdmin, String createdAt, String updatedAt) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}

