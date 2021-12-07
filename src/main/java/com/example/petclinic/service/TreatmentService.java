package com.example.petclinic.service;

import com.example.petclinic.exception.PetClinicException;
import com.example.petclinic.mapping.TreatmentMapper;
import com.example.petclinic.model.PetResponse;
import com.example.petclinic.model.TreatmentRequest;
import com.example.petclinic.model.TreatmentResponse;
import com.example.petclinic.persistence.entities.PetEntity;
import com.example.petclinic.persistence.entities.TreatmentEntity;
import com.example.petclinic.persistence.repository.OwnerRepository;
import com.example.petclinic.persistence.repository.PetRepository;
import com.example.petclinic.persistence.repository.TreatmentRepository;
import com.example.petclinic.rest.OwnerController;
import com.example.petclinic.rest.PetController;
import com.example.petclinic.rest.util.ErrorReturnCode;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TreatmentService {

    TreatmentMapper treatmentMapper = Mappers.getMapper(TreatmentMapper.class);
    private final TreatmentRepository treatmentRepository;
    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final PetService petService;

    public TreatmentService(TreatmentRepository treatmentRepository,
                            OwnerRepository ownerRepository,
                            PetRepository petRepository, PetService petService) {

        this.treatmentRepository = treatmentRepository;
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.petService = petService;
    }

    //ADD TREATMENT
    public TreatmentResponse addTreatment(TreatmentRequest request, Long petId) {

        PetEntity petEntity = petService.validatePet(petId);

        validateRequest(request);

        TreatmentEntity treatmentEntity = treatmentMapper.treatmentRequestToTreatmentEntity(request);
        treatmentEntity.setPet(petEntity);
        treatmentRepository.save(treatmentEntity);

        return treatmentMapper.treatmentEntityToTreatmentResponse(treatmentEntity);
    }

    private void validateRequest(TreatmentRequest request) {
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

    //GET TREATMENTS
    public List<TreatmentResponse> getTreatments(String description, LocalDate from, LocalDate to) {

        List<TreatmentResponse> responseList = treatmentMapper.treatmentEntityListToTreatmentResponseList(findByCriteria(description.trim(), from, to));

        responseList.forEach(treatmentResponse -> treatmentResponse.add(linkTo(methodOn(PetController.class)
                .getPetById(treatmentResponse.getPetId()))
                .withRel("Pet")));

        return responseList;
    }

    //UPDATE TREATMENT
    public TreatmentResponse updateTreatment(String description, Long treatmentId) {

        TreatmentEntity treatmentEntity = validateTreatment(treatmentId);

        updateTreatment(description, treatmentEntity);

        return treatmentMapper.treatmentEntityToTreatmentResponse(treatmentEntity);
    }

    //DELETE TREATMENT
    public TreatmentResponse deleteTreatment(Long treatmentId) {

        TreatmentEntity treatmentEntity = validateTreatment(treatmentId);

        TreatmentResponse response = treatmentMapper.treatmentEntityToTreatmentResponse(treatmentEntity);
        treatmentRepository.delete(treatmentEntity);
        return response;
    }

    //HELPER METHODS
    private List<TreatmentEntity> findByCriteria(String description, LocalDate from, LocalDate until) {
        return treatmentRepository.findAll((Specification<TreatmentEntity>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (description != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("description"), "%" + description + "%")));
            }
            if (from != null && until != null) {
                predicates.add(criteriaBuilder.between(root.get("treatmentDate"), from, until));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }

    TreatmentEntity validateTreatment(Long treatmentId) {
        Optional<TreatmentEntity> optionalTreatmentEntity = treatmentRepository.findById(treatmentId);

        if (optionalTreatmentEntity.isPresent()) {
            return optionalTreatmentEntity.get();
        } else {
            throw new PetClinicException(HttpStatus.NOT_FOUND, ErrorReturnCode.TREATMENT_NOT_FOUND);
        }
    }

    private void updateTreatment(String description, TreatmentEntity treatmentEntity) {
        if (!description.isBlank()) {
            treatmentEntity.setDescription(description.trim().toUpperCase());
        }

        treatmentRepository.save(treatmentEntity);
    }
}
