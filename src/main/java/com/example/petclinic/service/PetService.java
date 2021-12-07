package com.example.petclinic.service;

import com.example.petclinic.exception.PetClinicException;
import com.example.petclinic.mapping.OwnerMapper;
import com.example.petclinic.mapping.PetMapper;
import com.example.petclinic.model.OwnerRequest;
import com.example.petclinic.model.OwnerResponse;
import com.example.petclinic.model.PetRequest;
import com.example.petclinic.model.PetResponse;
import com.example.petclinic.persistence.entities.OwnerEntity;
import com.example.petclinic.persistence.entities.PetEntity;
import com.example.petclinic.persistence.repository.OwnerRepository;
import com.example.petclinic.persistence.repository.PetRepository;
import com.example.petclinic.rest.OwnerController;
import com.example.petclinic.rest.util.ErrorReturnCode;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PetService {

    PetMapper petMapper = Mappers.getMapper(PetMapper.class);
    OwnerMapper ownerMapper = Mappers.getMapper(OwnerMapper.class);
    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;
    private final OwnerService ownerService;

    public PetService(PetRepository petRepository, OwnerRepository ownerRepository, OwnerService ownerService) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
        this.ownerService = ownerService;
    }

    //ADD PET
    public PetResponse addPet(PetRequest request, Long ownerId) {

        validateRequest(request);

        OwnerEntity ownerEntity = ownerService.validateOwner(ownerId);

        PetEntity petEntity = petMapper.petRequestToPetEntity(request);
        petEntity.setOwner(ownerEntity);
        petRepository.save(petEntity);

        return petMapper.petEntityToPetResponse(petEntity);
    }

    //GET PETS
    public List<PetResponse> getPets(String name, String type) {

        List<PetResponse> responseList = petMapper.petResponseListFromPetEntityList(findByCriteria(name.trim(), type.trim()));

        responseList.forEach(petResponse -> petResponse.add(linkTo(methodOn(OwnerController.class)
                .getOwnerById(petResponse.getOwnerId()))
                .withRel("Owner")));

        return responseList;
    }

    //GET PETS BY OWNER ID
    public List<PetResponse> getPetsByOwnerId(Long ownerId) {

        OwnerEntity ownerEntity = ownerService.validateOwner(ownerId);

        return petMapper.petResponseListFromPetEntityList(petRepository.findPetEntitiesByOwner(ownerEntity));
    }

    //GET PET BY ID
    public PetResponse getPetById(Long petId) {
        PetEntity petEntity = validatePet(petId);

        return petMapper.petEntityToPetResponse(petEntity);
    }

    //UPDATE PET
    public PetResponse updatePet(String name, Long petId) {

        PetEntity petEntity = validatePet(petId);

        updatePet(name, petEntity);

        return petMapper.petEntityToPetResponse(petEntity);
    }

    //DELETE PET
    public PetResponse deletePet(Long petId) {

        PetEntity petEntity = validatePet(petId);

        PetResponse response = petMapper.petEntityToPetResponse(petEntity);
        petRepository.delete(petEntity);
        return response;
    }

    //HELPER METHODS
    private void updatePet(String name, PetEntity petEntity) {
        if (!name.isBlank()) {
            petEntity.setName(name.trim().toUpperCase());
        }

        petRepository.save(petEntity);
    }

    PetEntity validatePet(Long petId) {
        Optional<PetEntity> optionalPetEntity = petRepository.findById(petId);

        if (optionalPetEntity.isPresent()) {
            return optionalPetEntity.get();
        } else {
            throw new PetClinicException(HttpStatus.NOT_FOUND, ErrorReturnCode.PET_NOT_FOUND);
        }
    }

    private List<PetEntity> findByCriteria(String name, String type) {
        return petRepository.findAll((Specification<PetEntity>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
            }
            if (type != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("type"), "%" + type + "%")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }

    private void validateRequest(PetRequest request) {
        if (request.getName().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.NAME_MISSING);
        }

        if (request.getType().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.PET_TYPE_MISSING);
        }
    }
}
