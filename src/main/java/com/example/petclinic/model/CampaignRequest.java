package com.example.petclinic.model;

public class CampaignRequest {

    private String name;
    private Integer month;
    private Integer year;
    private String petType;
    private String petGender;

    public String getName() {
        return name.trim().toUpperCase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPetType() {
        return petType.trim().toUpperCase();
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetGender() {
        return petGender.trim().toUpperCase();
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }
}
