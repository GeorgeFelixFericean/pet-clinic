package com.example.petclinic.mapping;

import com.example.petclinic.model.TreatmentRequest;
import com.example.petclinic.model.TreatmentResponse;
import com.example.petclinic.persistence.entities.TreatmentEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TreatmentMapper {

    TreatmentEntity treatmentRequestToTreatmentEntity(TreatmentRequest treatmentRequest);

    TreatmentResponse treatmentEntityToTreatmentResponse(TreatmentEntity treatmentEntity);

    List<TreatmentResponse> treatmentEntityListToTreatmentResponseList(List<TreatmentEntity> treatmentEntityList);
}
