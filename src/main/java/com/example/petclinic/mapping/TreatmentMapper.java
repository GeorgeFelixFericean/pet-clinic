package com.example.petclinic.mapping;

import com.example.petclinic.model.AddTreatmentRequest;
import com.example.petclinic.model.TreatmentResponse;
import com.example.petclinic.persistence.entities.TreatmentEntity;
import org.mapstruct.Mapper;

@Mapper
public interface TreatmentMapper {

    TreatmentEntity addTreatmentRequestToTreatmentEntity(AddTreatmentRequest addTreatmentRequest);

    TreatmentResponse treatmentEntityToTreatmentResponse(TreatmentEntity treatmentEntity);
}
