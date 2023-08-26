package com.countries.controller;

import com.countries.model.CountryDensityDetail;
import com.countries.model.response.CountryDensityResponse;
import com.countries.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping(value = "/sortedByDensity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryDensityResponse> getSortedCountriesByDensity() {
        List<CountryDensityDetail> sortedDetails = countryService.getSortedCountriesByDensity();
        CountryDensityResponse response = new CountryDensityResponse(sortedDetails);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/asia-max-bordering-different-region")
    public String getAsianCountryWithMostBorderingDifferentRegion() {
        return "ASIA MAX BORDERING DIFFERENT REGION";
    }
}
