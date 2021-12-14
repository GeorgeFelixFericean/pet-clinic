package com.example.petclinic.rest;

import com.example.petclinic.model.OwnerRequest;
import com.example.petclinic.model.OwnerResponse;
import com.example.petclinic.service.OwnerService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
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
            method = RequestMethod.PUT)
    public ResponseEntity<OwnerResponse> updateOwner(
            @ApiParam(value = "The new owner name", required = true)
            @Valid
            @RequestParam(value = "name") String name
            ,

            @ApiParam(value = "The new owner address", required = true)
            @Valid
            @RequestParam(value = "address") String address
            ,
            @ApiParam(value = "The owner phone", required = true)
            @Valid
            @RequestParam(value = "phone") String phone
            ,
            @ApiParam(value = "The owner id", required = true)
            @PathVariable("ownerId") Long ownerId) {

        return ResponseEntity.ok(ownerService.updateOwner(name, address, phone, ownerId));
    }

    //DELETE OWNER
    @RequestMapping(
            value = "/admin/owners/{ownerId}",
            method = RequestMethod.DELETE)
    public ResponseEntity<OwnerResponse> deleteOwner(@ApiParam(value = "The owner id", required = true) @PathVariable("ownerId") Long ownerId) {

        return ResponseEntity.ok(ownerService.deleteOwner(ownerId));
    }
}
