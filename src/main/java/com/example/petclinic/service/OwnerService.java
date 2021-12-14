package com.example.petclinic.service;

import com.example.petclinic.exception.PetClinicException;
import com.example.petclinic.mapping.OwnerMapper;
import com.example.petclinic.model.OwnerRequest;
import com.example.petclinic.model.OwnerResponse;
import com.example.petclinic.persistence.entities.OwnerEntity;
import com.example.petclinic.persistence.repository.OwnerRepository;
import com.example.petclinic.rest.util.ErrorReturnCode;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    OwnerMapper ownerMapper = Mappers.getMapper(OwnerMapper.class);

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    //ADD OWNER
    public OwnerResponse addOwner(OwnerRequest request) {

        validateRequest(request);

        OwnerEntity ownerEntity = ownerMapper.addOwnerRequestToOwnerEntity(request);
        ownerRepository.save(ownerEntity);
        return ownerMapper.ownerEntityToOwnerResponse(ownerEntity);
    }

    //GET OWNERS
    public List<OwnerResponse> getOwners(String name, String address, String phone) {

        List<OwnerResponse> responseList = ownerMapper.ownerEntitiesToOwnerResponses(findByCriteria(name.trim(), address.trim(), phone.trim()));

        return responseList;
    }

    //GET OWNER BY ID
    public OwnerResponse getOwnerById(Long ownerId) {
        OwnerEntity ownerEntity = validateOwner(ownerId);

        return ownerMapper.ownerEntityToOwnerResponse(ownerEntity);
    }

    //UPDATE OWNER (ADMIN)
    public OwnerResponse updateOwner(String name, String address, String phone, Long ownerId) {

        OwnerEntity ownerEntity = validateOwner(ownerId);

        updateOwner(name, address, phone, ownerEntity);

        return ownerMapper.ownerEntityToOwnerResponse(ownerEntity);
    }

    //UPDATE OWNER - USERNAME AND PASSWORD (USER)
//    public OwnerResponse updateOwner(String username, String password) {
//
//    }

    //DELETE OWNER
    public OwnerResponse deleteOwner(Long ownerId) {

        OwnerEntity ownerEntity = validateOwner(ownerId);

        OwnerResponse response = ownerMapper.ownerEntityToOwnerResponse(ownerEntity);
        ownerRepository.delete(ownerEntity);
        return response;
    }

    //HELPER METHODS
    OwnerEntity validateOwner(Long ownerId) {
        Optional<OwnerEntity> optionalOwnerEntity = ownerRepository.findById(ownerId);

        if (optionalOwnerEntity.isPresent()) {
            return optionalOwnerEntity.get();
        } else {
            throw new PetClinicException(HttpStatus.NOT_FOUND, ErrorReturnCode.OWNER_NOT_FOUND);
        }
    }

    private void updateOwner(String name, String address, String phone, OwnerEntity ownerEntity) {
        if (!name.isBlank()) {
            ownerEntity.setName(name.trim().toUpperCase());
        }
        if (!address.isBlank()) {
            ownerEntity.setAddress(address.trim().toUpperCase());
        }
        if (!phone.isBlank()) {
            if (phone.length() == 10 && isValidPhoneNumber(phone)) {
                ownerEntity.setPhone(phone);
            } else {
                throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.INVALID_PHONE_NUMBER);
            }
        }
        ownerRepository.save(ownerEntity);
    }

    private List<OwnerEntity> findByCriteria(String name, String address, String phone) {
        return ownerRepository.findAll((Specification<OwnerEntity>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
            }
            if (address != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("address"), "%" + address + "%")));
            }
            if (phone != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("phone"), "%" + phone + "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }


    private void validateRequest(OwnerRequest request) {
        if (request.getName().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.NAME_MISSING);
        }

        if (request.getAddress().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.ADDRESS_MISSING);
        }

        if (request.getPhone().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.PHONE_MISSING);
        }

        if (request.getPhone().replaceAll(
                "\\s+", "").length() != 10 ||
                !isValidPhoneNumber(request.getPhone().replaceAll("\\s+", ""))) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.INVALID_PHONE_NUMBER);
        }

        if (request.getUsername().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.USERNAME_MISSING);
        }

        if (request.getPassword().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.PASSWORD_MISSING);
        }

        if (ownerRepository.findOwnerEntityByUsername(request.getUsername()).isPresent()) {
            throw new PetClinicException(HttpStatus.CONFLICT, ErrorReturnCode.USERNAME_ALREADY_EXISTS);
        }

        if (request.getRole().isBlank()) {
            throw new PetClinicException(HttpStatus.BAD_REQUEST, ErrorReturnCode.ROLE_MISSING);
        }
    }

    private boolean isValidPhoneNumber(String phone) {
        // Traverse the string from
        // start to end
        for (int i = 0; i < 10; i++) {

            // Check if the sepecified
            // character is a digit then
            // return true,
            // else return false
            if (!Character.isDigit(phone.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
