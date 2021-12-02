package com.example.petclinic.rest;

import com.example.petclinic.model.AddTreatmentRequest;
import com.example.petclinic.model.TreatmentResponse;
import com.example.petclinic.service.TreatmentService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @RequestMapping(
            value = "/owners/{ownerId}/pets/{petId}/treatments",
            produces = {"application/json;charset=utf-8"},
            consumes = {"application/json;charset=utf-8"},
            method = RequestMethod.POST)
    public ResponseEntity<TreatmentResponse> addTreatment(
            @ApiParam(value = "JSON payload", required = true)
            @Valid
            @RequestBody AddTreatmentRequest request
            ,
            @ApiParam(value = "The owner id", required = true) @PathVariable("ownerId") Long ownerId
            ,
            @ApiParam(value = "The pet id", required = true) @PathVariable("petId") Long petId) {

        return ResponseEntity.ok(treatmentService.addTreatment(request, ownerId, petId));
    }
}
