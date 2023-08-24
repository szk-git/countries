package com.countries.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    @GetMapping("/sorted-by-density")
    public List<String> getCountriesSortedByDensity() {
        return Collections.singletonList("SORTED BY DENSITY");
    }

    @GetMapping("/asia-max-bordering-different-region")
    public String getAsianCountryWithMostBorderingDifferentRegion() {
        return "ASIA MAX BORDERING DIFFERENT REGION";
    }
}
