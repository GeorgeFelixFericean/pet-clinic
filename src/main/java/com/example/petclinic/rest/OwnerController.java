package com.example.petclinic.rest;

import com.example.petclinic.model.AddOwnerRequest;
import com.example.petclinic.model.OwnerResponse;
import com.example.petclinic.service.OwnerService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @RequestMapping(
            value = "/owners",
            produces = {"application/json;charset=utf-8"},
            consumes = {"application/json;charset=utf-8"},
            method = RequestMethod.POST)
    public ResponseEntity<OwnerResponse> addOwner(
            @ApiParam(value = "JSON payload", required = true)
            @Valid
            @RequestBody AddOwnerRequest request) {

        return ResponseEntity.ok(ownerService.addOwner(request));
    }
}
