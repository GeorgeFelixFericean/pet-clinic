package com.example.petclinic.rest;

import com.example.petclinic.model.CampaignResponse;
import com.example.petclinic.model.PetResponse;
import com.example.petclinic.service.PetService;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
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
            method = RequestMethod.POST)
    public ResponseEntity<PetResponse> addPet(
            @ApiParam(value = "The pet name", required = true)
            @Valid
            @RequestParam(value = "name") String name
            ,
            @ApiParam(value = "The pet type", required = true)
            @Valid
            @RequestParam(value = "type") String type
            ,
            @ApiParam(value = "The pet photo")
            @Valid
            @RequestParam(value = "photo") MultipartFile photo
            ,
            @ApiParam(value = "The owner id", required = true)
            @PathVariable("ownerId") Long ownerId
            ,
            @ApiParam(value = "The pet gender", required = true)
            @Valid
            @RequestParam(value = "gender") String gender
            ) throws IOException {

        return ResponseEntity.ok(petService.addPet(name, type, photo, ownerId, gender));
    }

    //GET PETS
    @RequestMapping(
            value = "/admin/pets",
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.GET)
    public ResponseEntity<List<PetResponse>> getPets(
            String name,
            String phone,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate from,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate until) {

        return ResponseEntity.ok(petService.getPets(name, phone, from, until));
    }

    //PUBLIC
    //GET ALL PETS FROM CAMPAIGN
    @RequestMapping(value = "/public/campaign", method = RequestMethod.GET)
    public ResponseEntity<CampaignResponse> getCampaignPets() {

        return ResponseEntity.ok(petService.getCampaignPets());
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

    //GET PHOTO
    @RequestMapping(value = "/admin/pets/{petId}/photo", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long petId) {

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(petService.getPhoto(petId), headers, HttpStatus.ACCEPTED);
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
            @ApiParam(value = "The new pet photo", required = true)
            @Valid
            @RequestParam(value = "photo") MultipartFile photo
            ,
            @ApiParam(value = "The pet id", required = true)
            @PathVariable("petId") Long petId) throws IOException {

        return ResponseEntity.ok(petService.updatePet(name, photo, petId));
    }

    //DELETE PET
    @RequestMapping(
            value = "/admin/pets/{petId}",
            method = RequestMethod.DELETE)
    public ResponseEntity<PetResponse> deletePet(@ApiParam(value = "The pet id", required = true) @PathVariable("petId") Long petId) {

        return ResponseEntity.ok(petService.deletePet(petId));
    }
}
