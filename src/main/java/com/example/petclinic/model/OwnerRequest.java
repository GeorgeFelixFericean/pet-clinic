package com.example.petclinic.model;

public class OwnerRequest {
    private String name;
    private String address;
    private String phone;
    private String username;
    private String role;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role.trim().toUpperCase();
    }

    public void setRole(String role) {
        this.role = role;
    }
}
