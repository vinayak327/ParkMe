package com.example.parkme.Others;

public class Booking {
    private String name;
    private String email;
    private String phone;
    private String plateNo;
    private String durationFrom;
    private String durationTo;
    private String slotNo;

    public Booking() {
        // Default constructor required for Firebase
    }

    public Booking(String name, String email, String phone, String plateNo, String durationFrom, String durationTo, String slotNo) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.plateNo = plateNo;
        this.durationFrom = durationFrom;
        this.durationTo = durationTo;
        this.slotNo = slotNo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getDurationFrom() {
        return durationFrom;
    }

    public void setDurationFrom(String durationFrom) {
        this.durationFrom = durationFrom;
    }

    public String getDurationTo() {
        return durationTo;
    }

    public void setDurationTo(String durationTo) {
        this.durationTo = durationTo;
    }

    public String getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(String slotNo) {
        this.slotNo = slotNo;
    }
}
