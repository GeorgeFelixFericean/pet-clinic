package com.example.petclinic.rest;

import com.example.petclinic.model.OwnerRequest;
import com.example.petclinic.model.OwnerResponse;
import com.example.petclinic.model.PetRequest;
import com.example.petclinic.model.PetResponse;
import com.example.petclinic.service.PetService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    //ADD PET
    @RequestMapping(
            value = "/admin/owners/{ownerId}/pets",
            produces = {"application/json;charset=utf-8"},
            consumes = {"application/json;charset=utf-8"},
            method = RequestMethod.POST)
    public ResponseEntity<PetResponse> addPet(
            @ApiParam(value = "JSON payload", required = true)
            @Valid
            @RequestBody PetRequest request
            ,
            @ApiParam(value = "The owner id", required = true) @PathVariable("ownerId") Long ownerId) {

        return ResponseEntity.ok(petService.addPet(request, ownerId));
    }

    //GET PETS
    @RequestMapping(
            value = "/admin/pets",
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.GET)
    public ResponseEntity<List<PetResponse>> getPets(String name, String type) {

        return ResponseEntity.ok(petService.getPets(name, type));
    }

    //GET PETS BY OWNER ID
    @RequestMapping(
            value = "/admin/owners/{ownerId}/pets",
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.GET)
    public ResponseEntity<List<PetResponse>> getPetsByOwnerId(
            @ApiParam(value = "The owner id", required = true) @PathVariable("ownerId") Long ownerId) {

        return ResponseEntity.ok(petService.getPetsByOwnerId(ownerId));
    }

    //GET PET BY ID
    @RequestMapping(
            value = "/admin/pets/{petId}",
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.GET)
    public ResponseEntity<PetResponse> getPetById(
            @ApiParam(value = "The pet id", required = true) @PathVariable("petId") Long petId) {

        return ResponseEntity.ok(petService.getPetById(petId));
    }

    //UPDATE PET
    @RequestMapping(
            value = "/admin/pets/{petId}",
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.PUT)
    public ResponseEntity<PetResponse> updatePet(
            @ApiParam(value = "The new pet name", required = true)
            @Valid
            @RequestParam(value = "name") String name
            ,
            @ApiParam(value = "The pet id", required = true)
            @PathVariable("petId") Long petId) {

        return ResponseEntity.ok(petService.updatePet(name, petId));
    }

    //DELETE PET
    @RequestMapping(
            value = "/admin/pets/{petId}",
            method = RequestMethod.DELETE)
    public ResponseEntity<PetResponse> deletePet(@ApiParam(value = "The pet id", required = true) @PathVariable("petId") Long petId) {

        return ResponseEntity.ok(petService.deletePet(petId));
    }
}
