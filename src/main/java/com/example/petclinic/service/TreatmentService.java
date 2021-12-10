package com.example.petclinic.service;

import com.example.petclinic.exception.PetClinicException;
import com.example.petclinic.mapping.TreatmentMapper;
import com.example.petclinic.model.ReportResponse;
import com.example.petclinic.model.TreatmentRequest;
import com.example.petclinic.model.TreatmentResponse;
import com.example.petclinic.persistence.entities.OwnerEntity;
import com.example.petclinic.persistence.entities.PetEntity;
import com.example.petclinic.persistence.entities.TreatmentEntity;
import com.example.petclinic.persistence.repository.PetRepository;
import com.example.petclinic.persistence.repository.TreatmentRepository;
import com.example.petclinic.rest.util.ErrorReturnCode;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TreatmentService {

    TreatmentMapper treatmentMapper = Mappers.getMapper(TreatmentMapper.class);
    private final TreatmentRepository treatmentRepository;
    private final PetService petService;
    private final OwnerService ownerService;
    private final PetRepository petRepository;

    public TreatmentService(TreatmentRepository treatmentRepository,
                            PetService petService,
                            OwnerService ownerService,
                            PetRepository petRepository) {

        this.treatmentRepository = treatmentRepository;
        this.petService = petService;
        this.ownerService = ownerService;
        this.petRepository = petRepository;
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

    //GET TREATMENTS - ADMIN
    public List<TreatmentResponse> getTreatments(String description, LocalDate from, LocalDate until) {

        return treatmentMapper.treatmentEntityListToTreatmentResponseList(findByCriteriaAdmin(null, null, description.trim(), from, until));
    }

    //GET TREATMENTS - USER (ONE PET)
    public ReportResponse getTreatments(Long ownerId, Long petId, String description, LocalDate from, LocalDate until) {

//        OwnerEntity ownerEntity = ownerService.validateOwner(ownerId);
//
//        if (petId != null) {
//            petService.validatePet(petId);
//        }
//
//        List<TreatmentResponse> treatmentResponseList = treatmentMapper
//                .treatmentEntityListToTreatmentResponseList(findByCriteriaAdmin());
//
        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setTreatmentResponseList(treatmentMapper.treatmentEntityListToTreatmentResponseList(findByCriteriaAdmin(ownerId, petId, description.trim(), from, until)));
//        reportResponse.setTotalCost(getReportTotalCost(treatmentResponseList));
//
        return reportResponse;

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

    private List<TreatmentEntity> findByPets(List<PetEntity> petEntityList) {
        List<TreatmentEntity> treatmentEntityList = new ArrayList<>();

        for (PetEntity petEntity : petEntityList) {
            treatmentEntityList.addAll(treatmentRepository.findAllByPetId(petEntity.getId()));
        }

        return treatmentEntityList;
    }

    private BigDecimal getReportTotalCost(List<TreatmentResponse> treatmentResponseList) {
        BigDecimal result = BigDecimal.ZERO;
        for (TreatmentResponse treatmentResponse : treatmentResponseList) {
            result = result.add(treatmentResponse.getCost());
        }
        return result;
    }

    private List<TreatmentEntity> findByCriteriaAdmin(Long ownerId, Long petId, String description, LocalDate from, LocalDate until) {
        return treatmentRepository.findAll((Specification<TreatmentEntity>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (description != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("description"), "%" + description + "%")));
            }
            if (from != null && until != null) {
                predicates.add(criteriaBuilder.between(root.get("treatmentDate"), from, until));
            }
            Join<TreatmentEntity, PetEntity> petEntityJoin = root.join("pet");
            if (petId != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(petEntityJoin.get("id"), petId)));
            }
            Join<PetEntity, OwnerEntity> ownerEntityJoin = petEntityJoin.join("owner");
            if (petId != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(ownerEntityJoin.get("id"), ownerId)));
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
