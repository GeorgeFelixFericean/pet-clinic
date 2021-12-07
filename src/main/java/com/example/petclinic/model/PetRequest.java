package com.example.petclinic.model;

import com.example.petclinic.persistence.entities.OwnerEntity;

public class PetRequest {

    private String name;
    private String type;
    private byte[] photo;


    public String getName() {
        return name.trim().toUpperCase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type.trim().toUpperCase();
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

}
