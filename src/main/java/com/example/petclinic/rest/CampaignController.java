package com.example.petclinic.rest;

import com.example.petclinic.model.CampaignPetsResponse;
import com.example.petclinic.model.CampaignRequest;
import com.example.petclinic.model.CampaignResponse;
import com.example.petclinic.service.CampaignService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    //ADMIN
    //CREATE A CAMPAIGN
    @RequestMapping(
            value = "/admin/campaigns",
            produces = {"application/json;charset=utf-8"},
            consumes = {"application/json;charset=utf-8"},
            method = RequestMethod.POST)
    public ResponseEntity<CampaignResponse> addCampaign(
            @ApiParam(value = "JSON payload", required = true)
            @Valid
            @RequestBody CampaignRequest request) {

        return ResponseEntity.ok(campaignService.addCampaign(request));
    }

    //PUBLIC
    //GET PARTICIPATING PETS - THIS MONTH'S CAMPAIGN
    @RequestMapping(
            value = "/whosagoodboy",
            produces = {"application/json;charset=utf-8"},
            method = RequestMethod.GET)
    public ResponseEntity<CampaignPetsResponse> getPets() {

        return ResponseEntity.ok(campaignService.getPets());
    }


}
