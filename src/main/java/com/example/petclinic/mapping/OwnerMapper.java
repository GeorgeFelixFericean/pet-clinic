package com.example.petclinic.mapping;

import com.example.petclinic.model.AddOwnerRequest;
import com.example.petclinic.model.OwnerResponse;
import com.example.petclinic.persistence.entities.OwnerEntity;
import org.mapstruct.Mapper;

@Mapper
public interface OwnerMapper {

    OwnerEntity addOwnerRequestToOwnerEntity(AddOwnerRequest addOwnerRequest);

    OwnerResponse ownerEntityToOwnerResponse(OwnerEntity ownerEntity);
}
