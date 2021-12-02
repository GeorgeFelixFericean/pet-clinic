package com.example.petclinic.service;

import com.example.petclinic.exception.PetClinicException;
import com.example.petclinic.mapping.OwnerMapper;
import com.example.petclinic.model.AddOwnerRequest;
import com.example.petclinic.persistence.entities.OwnerEntity;
import com.example.petclinic.persistence.repository.OwnerRepository;
import com.example.petclinic.rest.util.ErrorReturnCode;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    OwnerMapper ownerMapper = Mappers.getMapper(OwnerMapper.class);

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public com.example.petclinic.model.OwnerResponse addOwner(AddOwnerRequest request) {

        validateRequest(request);

        OwnerEntity ownerEntity = ownerMapper.addOwnerRequestToOwnerEntity(request);
        ownerRepository.save(ownerEntity);
        return ownerMapper.ownerEntityToOwnerResponse(ownerEntity);
    }

    private void validateRequest(AddOwnerRequest request) {
        if (request.getName().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.NAME_MISSING);
        }

        if (request.getAddress().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.ADDRESS_MISSING);
        }

        if (request.getPhone().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.PHONE_MISSING);
        }

        if (request.getPhone().replaceAll("\\s+","").length() != 10) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.INVALID_PHONE_NUMBER);
        }
    }
}
