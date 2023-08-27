package com.countries.controller;

import com.countries.model.dto.CountryDensityDetailDTO;
import com.countries.model.dto.CountryMostBordersDTO;
import com.countries.model.response.CountryDensityResponse;
import com.countries.model.response.CountryMostBordersResponse;
import com.countries.service.CountryAnalyzer;
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

    private final CountryAnalyzer countryAnalyzer;

    @Autowired
    public CountryController(CountryAnalyzer countryAnalyzer) {
        this.countryAnalyzer = countryAnalyzer;
    }

    @GetMapping(value = "/sortedByDensity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryDensityResponse> getSortedCountriesByDensity() {
        List<CountryDensityDetailDTO> sortedDetails = countryAnalyzer.getSortedCountriesByDensity();
        CountryDensityResponse response = new CountryDensityResponse(sortedDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/asiaMaxBorderingDifferentRegion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryMostBordersResponse> getAsianCountryWithMostBorderingDifferentRegion() {
        CountryMostBordersDTO country = countryAnalyzer.getAsianCountryWithMostBorderingDifferentRegion();
        CountryMostBordersResponse response = new CountryMostBordersResponse(country);
        return ResponseEntity.ok(response);
    }
}
