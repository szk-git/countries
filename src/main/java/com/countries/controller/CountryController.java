package com.countries.controller;

import com.countries.model.dto.CountryDensityDetailDTO;
import com.countries.model.dto.CountryMostBordersDTO;
import com.countries.model.response.CountryDensityResponse;
import com.countries.model.response.CountryMostBordersResponse;
import com.countries.service.CountryAnalyzerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "Country APIs")
@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryAnalyzerService countryAnalyzerService;

    @Autowired
    public CountryController(CountryAnalyzerService countryAnalyzerService) {
        this.countryAnalyzerService = countryAnalyzerService;
    }

    @ApiOperation("Get a list of countries sorted by population density")
    @GetMapping(value = "/sortedByDensity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryDensityResponse> getSortedCountriesByDensity() {
        List<CountryDensityDetailDTO> sortedDetails = countryAnalyzerService.getSortedCountriesByDensity();
        CountryDensityResponse response = new CountryDensityResponse(sortedDetails);
        return ResponseEntity.ok(response);
    }

    @ApiOperation("Get the Asian country with the most bordering different regions")
    @GetMapping(value = "/asiaMaxBorderingDifferentRegion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CountryMostBordersResponse> getAsianCountryWithMostBorderingDifferentRegion() {
        CountryMostBordersDTO country = countryAnalyzerService.getAsianCountryWithMostBorderingDifferentRegion();
        CountryMostBordersResponse response = new CountryMostBordersResponse(country);
        return ResponseEntity.ok(response);
    }
}
