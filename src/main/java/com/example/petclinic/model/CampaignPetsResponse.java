package com.example.petclinic.model;

import java.util.List;

public class CampaignPetsResponse {

    private String campaignName;
    private List<PetCampaignResponse> pets;

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public List<PetCampaignResponse> getPets() {
        return pets;
    }

    public void setPets(List<PetCampaignResponse> pets) {
        this.pets = pets;
    }
}
