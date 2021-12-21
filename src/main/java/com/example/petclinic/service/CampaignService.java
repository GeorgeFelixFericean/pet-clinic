package com.example.petclinic.service;

import com.example.petclinic.exception.PetClinicException;
import com.example.petclinic.mapping.CampaignMapper;
import com.example.petclinic.mapping.PetMapper;
import com.example.petclinic.model.CampaignPetsResponse;
import com.example.petclinic.model.CampaignRequest;
import com.example.petclinic.model.CampaignResponse;
import com.example.petclinic.model.PetCampaignResponse;
import com.example.petclinic.persistence.entities.CampaignEntity;
import com.example.petclinic.persistence.repository.CampaignRepository;
import com.example.petclinic.rest.PetController;
import com.example.petclinic.rest.util.ErrorReturnCode;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CampaignService {

    PetMapper petMapper = Mappers.getMapper(PetMapper.class);
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

    //GET PETS
    public CampaignPetsResponse getPets() {

        CampaignPetsResponse campaignPetsResponse = new CampaignPetsResponse();


        //todo - change hard-coded values(month and year)
        Optional<CampaignEntity> optionalCampaignEntity = campaignRepository.findCampaignEntityByMonthAndYear(1, 2022);

        if (optionalCampaignEntity.isPresent()) {
            campaignPetsResponse.setCampaignName(optionalCampaignEntity.get().getName());
            List<PetCampaignResponse> petCampaignResponseList = petMapper.petCampaignResponseFromPetEntityList(optionalCampaignEntity.get().getPets());
            petCampaignResponseList.forEach(pet -> pet.add(linkTo(methodOn(PetController.class)
                    .getPhoto(pet.getId()))
                    .withRel("Photo")));
            campaignPetsResponse.setPets(petCampaignResponseList);
        } else {
            campaignPetsResponse.setCampaignName("No campaign in progress at the moment");
            campaignPetsResponse.setPets(new ArrayList<>());
        }
        return campaignPetsResponse;
    }

    //HELPER METHODS
    private void validateRequest(CampaignRequest request) {
        if (request.getMonth() <= LocalDate.now().getMonthValue() && request.getYear() <= LocalDate.now().getYear()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.INVALID_DATE);
        }

        if (campaignRepository.findCampaignEntityByMonthAndYear(request.getMonth(), request.getYear()).isPresent()) {
            throw new PetClinicException(HttpStatus.CONFLICT, ErrorReturnCode.CAMPAIGN_ALREADY_EXISTS);
        }
    }
}
