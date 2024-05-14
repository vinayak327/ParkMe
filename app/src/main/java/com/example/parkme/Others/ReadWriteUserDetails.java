package com.example.parkme.Others;

public class ReadWriteUserDetails {
    public String fullName, dob, gender, mobile;

    public ReadWriteUserDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(ReadWriteUserDetails.class)
    }

    public ReadWriteUserDetails(String fullName, String dob, String gender, String mobile) {
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.mobile = mobile;
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
