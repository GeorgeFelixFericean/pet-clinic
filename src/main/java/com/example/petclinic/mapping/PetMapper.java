package com.example.petclinic.mapping;

import com.example.petclinic.model.PetRequest;
import com.example.petclinic.model.PetResponse;
import com.example.petclinic.persistence.entities.PetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface PetMapper {


    PetEntity petRequestToPetEntity(PetRequest petRequest);

    @Mapping(target = "ownerId", source = "petEntity.owner.id")
    PetResponse petEntityToPetResponse(PetEntity petEntity);

    List<PetResponse> petResponseListFromPetEntityList(List<PetEntity> petEntityList);

}
