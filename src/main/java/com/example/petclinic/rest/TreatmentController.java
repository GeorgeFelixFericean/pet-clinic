package com.example.petclinic.rest;

import com.example.petclinic.model.PetResponse;
import com.example.petclinic.model.TreatmentRequest;
import com.example.petclinic.model.TreatmentResponse;
import com.example.petclinic.service.TreatmentService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    //ADD TREATMENT
    @RequestMapping(
            value = "/admin/pets/{petId}/treatments",
            produces = {"application/json;charset=utf-8"},
            consumes = {"application/json;charset=utf-8"},
            method = RequestMethod.POST)
    public ResponseEntity<TreatmentResponse> addTreatment(
            @ApiParam(value = "JSON payload", required = true)
            @Valid
            @RequestBody TreatmentRequest request
            ,
            @ApiParam(value = "The pet id", required = true) @PathVariable("petId") Long petId) {

        return ResponseEntity.ok(treatmentService.addTreatment(request, petId));
    }

    //GET TREATMENTS
    @RequestMapping(
            value = "/admin/treatments",
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.GET)
    public ResponseEntity<List<TreatmentResponse>> getTreatments(String description, LocalDate from, LocalDate until) {

        return ResponseEntity.ok(treatmentService.getTreatments(description, from, until));
    }

    //UPDATE TREATMENT
    @RequestMapping(
            value = "/admin/treatments/{treatmentId}",
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.PUT)
    public ResponseEntity<TreatmentResponse> updateTreatment(
            @ApiParam(value = "The new treatment description", required = true)
            @Valid
            @RequestParam(value = "description") String description
            ,
            @ApiParam(value = "The treatment id", required = true)
            @PathVariable("treatmentId") Long treatmentId) {

        return ResponseEntity.ok(treatmentService.updateTreatment(description, treatmentId));
    }

    //DELETE TREATMENT
    @RequestMapping(
            value = "/admin/treatments/{treatmentId}",
            method = RequestMethod.DELETE)
    public ResponseEntity<TreatmentResponse> deleteTreatment(@ApiParam(value = "The treatment id", required = true) @PathVariable("treatmentId") Long treatmentId) {

        return ResponseEntity.ok(treatmentService.deleteTreatment(treatmentId));
    }
}
