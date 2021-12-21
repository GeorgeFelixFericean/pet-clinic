package com.example.petclinic.service;

import com.example.petclinic.exception.PetClinicException;
import com.example.petclinic.mapping.CampaignMapper;
import com.example.petclinic.model.CampaignRequest;
import com.example.petclinic.model.CampaignResponse;
import com.example.petclinic.persistence.entities.CampaignEntity;
import com.example.petclinic.persistence.repository.CampaignRepository;
import com.example.petclinic.rest.util.ErrorReturnCode;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;

@Service
public class CampaignService {

    CampaignMapper campaignMapper = Mappers.getMapper(CampaignMapper.class);
    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    //ADD CAMPAIGN
    public CampaignResponse addCampaign(CampaignRequest request) {

        validateRequest(request);

        CampaignEntity campaignEntity = campaignMapper.campaignRequestToCampaignEntity(request);
        campaignRepository.save(campaignEntity);

        CampaignResponse campaignResponse = new CampaignResponse();
        campaignResponse.setCampaignName(campaignEntity.getName());
        campaignResponse.setMonth(Month.of(campaignEntity.getMonth()) + " " + campaignEntity.getYear());
        campaignResponse.setPets("For " + campaignEntity.getPetGender() + " " + campaignEntity.getPetType() + "s");

        return campaignResponse;
    }

    private void validateRequest(CampaignRequest request) {
        if (request.getMonth() <= LocalDate.now().getMonthValue() && request.getYear() <= LocalDate.now().getYear()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.INVALID_DATE);
        }

        if (campaignRepository.findCampaignEntityByMonthAndYear(request.getMonth(), request.getYear()).isPresent()) {
            throw new PetClinicException(HttpStatus.CONFLICT, ErrorReturnCode.CAMPAIGN_ALREADY_EXISTS);
        }
    }
}
