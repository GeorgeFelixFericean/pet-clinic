package com.example.petclinic.mapping;

import com.example.petclinic.model.CampaignRequest;
import com.example.petclinic.model.CampaignResponse;
import com.example.petclinic.persistence.entities.CampaignEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CampaignMapper {

    CampaignEntity campaignRequestToCampaignEntity(CampaignRequest request);

}
