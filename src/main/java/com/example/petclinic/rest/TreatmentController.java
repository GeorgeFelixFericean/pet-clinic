package com.example.petclinic.rest;

import com.example.petclinic.model.ReportResponse;
import com.example.petclinic.model.TreatmentRequest;
import com.example.petclinic.model.TreatmentResponse;
import com.example.petclinic.service.TreatmentService;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
            @ApiParam(value = "The pet id", required = true)
            @PathVariable("petId") Long petId) {

        return ResponseEntity.ok(treatmentService.addTreatment(request, petId));
    }

    //GET TREATMENTS - ADMIN
    @RequestMapping(
            value = "/admin/treatments",
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.GET)
    public ResponseEntity<List<TreatmentResponse>> getTreatmentsAdmin(
            String description
            ,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate from
            ,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate until) {

        return ResponseEntity.ok(treatmentService.getTreatmentsAdmin(description, from, until));
    }

    //GET TREATMENTS - USER
    @RequestMapping(
            value = {"/user/owners/{ownerId}/pets/{petId}/treatments", "/user/owners/{ownerId}/pets/treatments"},
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.GET)
    public ResponseEntity<ReportResponse> getTreatmentsUser(
            @ApiParam(value = "The owner id", required = true)
            @PathVariable("ownerId") Long ownerId
            ,
            @ApiParam(value = "The pet id")
            @PathVariable("petId") Optional<Long> optionalPetId
            ,
            String description
            ,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate from
            ,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate until) {

        return optionalPetId.map(petId -> ResponseEntity.ok(treatmentService.getTreatmentsUser(ownerId, petId, description, from, until)))
                .orElseGet(() -> ResponseEntity.ok(treatmentService.getTreatmentsUser(ownerId, null, description, from, until)));
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
