package com.example.petclinic.rest;

import com.example.petclinic.model.AddPetRequest;
import com.example.petclinic.model.PetResponse;
import com.example.petclinic.service.PetService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @RequestMapping(
            value = "/owners/{ownerId}/pets",
            produces = {"application/json;charset=utf-8"},
            consumes = {"application/json;charset=utf-8"},
            method = RequestMethod.POST)
    public ResponseEntity<PetResponse> addPet(
            @ApiParam(value = "JSON payload", required = true)
            @Valid
            @RequestBody AddPetRequest request
            ,
            @ApiParam(value = "The owner id", required = true) @PathVariable("ownerId") Long ownerId) {

        return ResponseEntity.ok(petService.addPet(request, ownerId));
    }
}
