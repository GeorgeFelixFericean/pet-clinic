package com.example.petclinic.service;

import com.example.petclinic.exception.PetClinicException;
import com.example.petclinic.mapping.PetMapper;
import com.example.petclinic.model.PetResponse;
import com.example.petclinic.persistence.entities.OwnerEntity;
import com.example.petclinic.persistence.entities.PetEntity;
import com.example.petclinic.persistence.entities.TreatmentEntity;
import com.example.petclinic.persistence.repository.PetRepository;
import com.example.petclinic.rest.PetController;
import com.example.petclinic.rest.util.ErrorReturnCode;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PetService {

    PetMapper petMapper = Mappers.getMapper(PetMapper.class);
    private final PetRepository petRepository;
    private final OwnerService ownerService;

    public PetService(PetRepository petRepository, OwnerService ownerService) {
        this.petRepository = petRepository;
        this.ownerService = ownerService;
    }

    //ADD PET
    public PetResponse addPet(String name, String type, MultipartFile photo, Long ownerId) throws IOException {

        validateRequest(name, type);

        OwnerEntity ownerEntity = ownerService.validateOwner(ownerId);

        PetEntity petEntity = new PetEntity();
        petEntity.setOwner(ownerEntity);
        petEntity.setName(name.trim().toUpperCase());
        petEntity.setType(type.trim().toUpperCase());
        petEntity.setPhoto(photo.getBytes());
        petRepository.save(petEntity);

        PetResponse petResponse = petMapper.petEntityToPetResponse(petEntity);
        petResponse.add(linkTo(methodOn(PetController.class).getPhoto(petResponse.getId())).withRel("Photo"));

        return petResponse;
    }

    //GET PETS
    public List<PetResponse> getPets(String name, String phone, LocalDate from, LocalDate until) {

        List<PetResponse> responseList = petMapper
                .petResponseListFromPetEntityList(findByCriteria(name.trim(), phone.trim(), from, until));

        responseList.forEach(petResponse -> petResponse.add(linkTo(methodOn(PetController.class)
                .getPhoto(petResponse.getId()))
                .withRel("Photo")));

        return responseList.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    //GET PETS BY OWNER ID
    public List<PetResponse> getPetsByOwnerId(Long ownerId) {

        OwnerEntity ownerEntity = ownerService.validateOwner(ownerId);

        List<PetResponse> responseList = petMapper
                .petResponseListFromPetEntityList(petRepository.findPetEntitiesByOwner(ownerEntity));

        responseList.forEach(petResponse -> petResponse.add(linkTo(methodOn(PetController.class)
                .getPhoto(petResponse.getId()))
                .withRel("Photo")));

        return responseList;
    }

    //GET PET BY ID
    public PetResponse getPetById(Long petId) {
        PetEntity petEntity = validatePet(petId);

        PetResponse petResponse = petMapper.petEntityToPetResponse(petEntity);
        petResponse.add(linkTo(methodOn(PetController.class).getPhoto(petResponse.getId())).withRel("Photo"));

        return petResponse;
    }

    //GET PHOTO
    public byte[] getPhoto(Long petId) {
        PetEntity petEntity = validatePet(petId);

        return petEntity.getPhoto();
    }

    //UPDATE PET
    public PetResponse updatePet(String name, MultipartFile photo, Long petId) throws IOException {

        PetEntity petEntity = validatePet(petId);

        updatePet(name, photo, petEntity);

        PetResponse petResponse = petMapper.petEntityToPetResponse(petEntity);
        petResponse.add(linkTo(methodOn(PetController.class).getPhoto(petResponse.getId())).withRel("Photo"));

        return petResponse;
    }

    //DELETE PET
    public PetResponse deletePet(Long petId) {

        PetEntity petEntity = validatePet(petId);

        PetResponse response = petMapper.petEntityToPetResponse(petEntity);

        petRepository.delete(petEntity);

        return response;
    }

    //HELPER METHODS
    private void updatePet(String name, MultipartFile photo, PetEntity petEntity) throws IOException {
        if (!name.isBlank()) {
            petEntity.setName(name.trim().toUpperCase());
        }

        if (photo.getBytes().length > 0) {
            petEntity.setPhoto(photo.getBytes());
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

    private List<PetEntity> findByCriteria(String name, String phone, LocalDate from, LocalDate until) {
        return petRepository.findAll((Specification<PetEntity>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isBlank()) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
            }
            Join<PetEntity, OwnerEntity> ownerEntityJoin = root.join("owner");
            if (phone != null && !phone.isBlank()) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(ownerEntityJoin.get("phone"), "%" + phone + "%")));
            }
            Join<PetEntity, TreatmentEntity> treatmentEntityJoin = root.join("treatments", JoinType.LEFT);
            if (from != null && until != null) {
                predicates.add(criteriaBuilder.between(treatmentEntityJoin.get("treatmentDate"), from, until));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }


    private void validateRequest(String name, String type) {
        if (name.isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.NAME_MISSING);
        }

        if (type.isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.PET_TYPE_MISSING);
        }
    }
}
