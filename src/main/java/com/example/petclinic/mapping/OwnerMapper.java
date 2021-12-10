package com.example.petclinic.mapping;

import com.example.petclinic.model.OwnerRequest;
import com.example.petclinic.model.OwnerResponse;
import com.example.petclinic.persistence.entities.OwnerEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OwnerMapper {

    OwnerEntity addOwnerRequestToOwnerEntity(OwnerRequest ownerRequest);

    OwnerResponse ownerEntityToOwnerResponse(OwnerEntity ownerEntity);

    List<OwnerResponse> ownerEntitiesToOwnerResponses(List<OwnerEntity> ownerEntities);

}
