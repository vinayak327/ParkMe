package com.example.parkme.Others;

public class User {
    private String fullName;
    private String dob;
    private String mobile;

    // Required empty constructor
    public User() {
    }

    public User(String fullName, String dob, String mobile) {
        this.fullName = fullName;
        this.dob = dob;
        this.mobile = mobile;
    }

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

