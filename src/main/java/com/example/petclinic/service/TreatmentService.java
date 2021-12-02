package com.example.petclinic.service;

import com.example.petclinic.exception.PetClinicException;
import com.example.petclinic.mapping.PetMapper;
import com.example.petclinic.mapping.TreatmentMapper;
import com.example.petclinic.model.AddPetRequest;
import com.example.petclinic.model.AddTreatmentRequest;
import com.example.petclinic.model.TreatmentResponse;
import com.example.petclinic.persistence.entities.OwnerEntity;
import com.example.petclinic.persistence.entities.PetEntity;
import com.example.petclinic.persistence.entities.TreatmentEntity;
import com.example.petclinic.persistence.repository.OwnerRepository;
import com.example.petclinic.persistence.repository.PetRepository;
import com.example.petclinic.persistence.repository.TreatmentRepository;
import com.example.petclinic.rest.util.ErrorReturnCode;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TreatmentService {

    TreatmentMapper treatmentMapper = Mappers.getMapper(TreatmentMapper.class);
    private final TreatmentRepository treatmentRepository;
    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;

    public TreatmentService(TreatmentRepository treatmentRepository,
                            OwnerRepository ownerRepository,
                            PetRepository petRepository) {

        this.treatmentRepository = treatmentRepository;
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
    }

    public TreatmentResponse addTreatment(AddTreatmentRequest request, Long ownerId, Long petId) {

        OwnerEntity ownerEntity;
        PetEntity petEntity;

        Optional<OwnerEntity> optionalOwnerEntity = ownerRepository.findById(ownerId);
        if (optionalOwnerEntity.isPresent()) {
            ownerEntity = optionalOwnerEntity.get();
        } else {
            throw new PetClinicException(HttpStatus.NOT_FOUND, ErrorReturnCode.OWNER_NOT_FOUND);
        }

        Optional<PetEntity> optionalPetEntity = petRepository.findById(petId);
        if (optionalPetEntity.isPresent()) {
            petEntity = optionalPetEntity.get();
        } else {
            throw new PetClinicException(HttpStatus.NOT_FOUND, ErrorReturnCode.PET_NOT_FOUND);
        }

//        validateRequest(request);

        TreatmentEntity treatmentEntity = treatmentMapper.addTreatmentRequestToTreatmentEntity(request);
        treatmentEntity.setPet(petEntity);
        treatmentRepository.save(treatmentEntity);

        return treatmentMapper.treatmentEntityToTreatmentResponse(treatmentEntity);
    }

    private void validateRequest(AddTreatmentRequest request) {
        if (request.getDescription().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.DESCRIPTION_MISSING);
        }

        if (request.getCost() == null) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.COST_MISSING);
        }

        if (request.getTreatmentDate() == null) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.DATE_MISSING);
        }
    }
}
