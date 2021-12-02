package com.example.petclinic.mapping;

import com.example.petclinic.model.AddPetRequest;
import com.example.petclinic.model.PetResponse;
import com.example.petclinic.persistence.entities.PetEntity;
import org.mapstruct.Mapper;

@Mapper
public interface PetMapper {

    PetEntity addPetRequestToPetEntity(AddPetRequest addPetRequest);

    PetResponse petEntityToPetResponse(PetEntity petEntity);
}
