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

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.Principal;
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
                            OwnerService ownerService, PetRepository petRepository) {

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
    public List<TreatmentResponse> getTreatmentsAdmin(String description, LocalDate from, LocalDate until) {

        return treatmentMapper.treatmentEntityListToTreatmentResponseList(findByCriteria(description.trim(), from, until));
    }

    //GET TREATMENTS - USER
    public ReportResponse getTreatmentsUser(Long ownerId, Long petId, LocalDate from, LocalDate until, HttpServletRequest request) {

        Principal user = request.getUserPrincipal();
        OwnerEntity ownerEntity = ownerService.validateOwner(ownerId);

        if (!user.getName().equals(ownerEntity.getUsername())) {
            throw new PetClinicException(HttpStatus.UNAUTHORIZED, ErrorReturnCode.UNAUTHORIZED);
        }

        if (petId != null) {
            return getReportForOnePet(ownerEntity, petId, from, until);
        } else {
            return getReportForAllPets(ownerEntity, from, until);
        }
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
    private ReportResponse getReportForAllPets(OwnerEntity ownerEntity, LocalDate from, LocalDate until) {
        ReportResponse response = new ReportResponse();
        List<PetEntity> petEntityList = petRepository.findPetEntitiesByOwner(ownerEntity);
        List<TreatmentEntity> treatmentEntityList = new ArrayList<>();
        for (PetEntity petEntity : petEntityList) {
            List<TreatmentEntity> treatmentEntities;
            if (from == null || until == null) {
                treatmentEntities = treatmentRepository.findAllByPet(petEntity);
            } else {
                treatmentEntities = treatmentRepository.findAllByPetAndTreatmentDateBetween(petEntity, from, until);
            }
            treatmentEntityList.addAll(treatmentEntities);
        }
        List<TreatmentResponse> treatmentResponseList = treatmentMapper.treatmentEntityListToTreatmentResponseList(treatmentEntityList);
        response.setTreatmentResponseList(treatmentResponseList);
        response.setTotalCost(getReportTotalCost(treatmentResponseList));

        return response;
    }

    private ReportResponse getReportForOnePet(OwnerEntity ownerEntity, Long petId, LocalDate from, LocalDate until) {
        ReportResponse response = new ReportResponse();

        Optional<PetEntity> petEntity = petRepository.findPetEntityByOwnerAndId(ownerEntity, petId);
        if (petEntity.isEmpty()) {
            throw new PetClinicException(HttpStatus.NOT_FOUND, ErrorReturnCode.PET_NOT_FOUND);
        }

        List<TreatmentResponse> treatmentResponseList;

        if (from == null || until == null) {
            treatmentResponseList = treatmentMapper.treatmentEntityListToTreatmentResponseList(treatmentRepository
                    .findAllByPet(petEntity.get()));
        } else {
            treatmentResponseList = treatmentMapper
                    .treatmentEntityListToTreatmentResponseList(treatmentRepository
                            .findAllByPetAndTreatmentDateBetween(petEntity.get(), from, until));
        }
        response.setTreatmentResponseList(treatmentResponseList);
        response.setTotalCost(getReportTotalCost(treatmentResponseList));

        return response;
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

    private BigDecimal getReportTotalCost(List<TreatmentResponse> treatmentResponseList) {
        BigDecimal result = BigDecimal.ZERO;
        for (TreatmentResponse treatmentResponse : treatmentResponseList) {
            result = result.add(treatmentResponse.getCost());
        }
        return result;
    }

    private List<TreatmentEntity> findByCriteria(String description, LocalDate from, LocalDate until) {
        return treatmentRepository.findAll((Specification<TreatmentEntity>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (description != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("description"), "%" + description + "%")));
            }

            if (from != null && until != null) {
                predicates.add(criteriaBuilder.between(root.get("treatmentDate"), from, until));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
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
