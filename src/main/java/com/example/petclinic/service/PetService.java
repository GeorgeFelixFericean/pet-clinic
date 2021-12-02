package com.example.petclinic.service;

import com.example.petclinic.exception.PetClinicException;
import com.example.petclinic.mapping.PetMapper;
import com.example.petclinic.model.AddPetRequest;
import com.example.petclinic.model.PetResponse;
import com.example.petclinic.persistence.entities.OwnerEntity;
import com.example.petclinic.persistence.entities.PetEntity;
import com.example.petclinic.persistence.repository.OwnerRepository;
import com.example.petclinic.persistence.repository.PetRepository;
import com.example.petclinic.rest.util.ErrorReturnCode;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetService {

    PetMapper petMapper = Mappers.getMapper(PetMapper.class);
    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    public PetService(PetRepository petRepository, OwnerRepository ownerRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
    }

    public PetResponse addPet(AddPetRequest request, Long ownerId) {

        Optional<OwnerEntity> optionalOwnerEntity = ownerRepository.findById(ownerId);
        OwnerEntity ownerEntity;
        if (optionalOwnerEntity.isPresent()) {
            ownerEntity = optionalOwnerEntity.get();
        } else {
            throw new PetClinicException(HttpStatus.NOT_FOUND, ErrorReturnCode.OWNER_NOT_FOUND);
        }

        validateRequest(request);

        PetEntity petEntity = petMapper.addPetRequestToPetEntity(request);
        petEntity.setOwner(ownerEntity);
        petRepository.save(petEntity);

        return petMapper.petEntityToPetResponse(petEntity);
    }

    private void validateRequest(AddPetRequest request) {
        if (request.getName().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.NAME_MISSING);
        }

        if (request.getType().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.PET_TYPE_MISSING);
        }
    }
}
