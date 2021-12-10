package com.example.petclinic.mapping;

import com.example.petclinic.model.PetResponse;
import com.example.petclinic.persistence.entities.PetEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PetMapper {

    PetResponse petEntityToPetResponse(PetEntity petEntity);

    List<PetResponse> petResponseListFromPetEntityList(List<PetEntity> petEntityList);

}
