package com.example.petclinic.rest;

import com.example.petclinic.model.OwnerRequest;
import com.example.petclinic.model.OwnerResponse;
import com.example.petclinic.service.OwnerService;
import com.example.petclinic.service.PetService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OwnerController {

    private final OwnerService ownerService;
    private final PetService petService;

    public OwnerController(OwnerService ownerService, PetService petService) {
        this.ownerService = ownerService;
        this.petService = petService;
    }

    //ADD OWNER
    @RequestMapping(
            value = "/admin/owners",
            produces = {"application/json;charset=utf-8"},
            consumes = {"application/json;charset=utf-8"},
            method = RequestMethod.POST)
    public ResponseEntity<OwnerResponse> addOwner(
            @ApiParam(value = "JSON payload", required = true)
            @Valid
            @RequestBody OwnerRequest request) {

        return ResponseEntity.ok(ownerService.addOwner(request));
    }

    //GET OWNERS
    @RequestMapping(
            value = "/admin/owners",
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.GET)
    public ResponseEntity<List<OwnerResponse>> getOwners(String name, String address, String phone) {

        return ResponseEntity.ok(ownerService.getOwners(name, address, phone));
    }

    //GET OWNER BY ID
    @RequestMapping(
            value = "/admin/owners/{ownerId}",
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.GET)
    public ResponseEntity<OwnerResponse> getOwnerById(
            @ApiParam(value = "The owner id", required = true) @PathVariable("ownerId") Long ownerId) {

        return ResponseEntity.ok(ownerService.getOwnerById(ownerId));
    }

    //UPDATE OWNER
    @RequestMapping(
            value = "/admin/owners/{ownerId}",
            produces = {"application/json;charset=utf-8"},
            consumes = {"application/json;charset=utf-8"},
            method = RequestMethod.PUT)
    public ResponseEntity<OwnerResponse> updateOwner(
            @ApiParam(value = "JSON payload", required = true)
            @Valid
            @RequestBody OwnerRequest request
            ,
            @ApiParam(value = "The owner id", required = true) @PathVariable("ownerId") Long ownerId) {

        return ResponseEntity.ok(ownerService.updateOwner(request, ownerId));
    }

    //DELETE OWNER
    @RequestMapping(
            value = "/admin/owners/{ownerId}",
            method = RequestMethod.DELETE)
    public ResponseEntity<OwnerResponse> deleteOwner(@ApiParam(value = "The owner id", required = true) @PathVariable("ownerId") Long ownerId) {

        return ResponseEntity.ok(ownerService.deleteOwner(ownerId));
    }
}
