package com.example.petclinic.model;

public class AddOwnerRequest {
    private String name;
    private String address;
    private String phone;

    public String getName() {
        return name.trim().toUpperCase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address.trim().toUpperCase();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone.replaceAll("\\s+","");
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
