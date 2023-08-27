package com.countries.service;

import com.countries.model.CountriesList;
import com.countries.model.Country;
import com.countries.utils.ApiUrlConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class CountryDataService {

    private final ApiUrlConstructor apiUrlConstructor;

    private final RestTemplate restTemplate;

    @Autowired
    public CountryDataService(RestTemplate restTemplate, ApiUrlConstructor apiUrlConstructor) {
        this.restTemplate = restTemplate;
        this.apiUrlConstructor = apiUrlConstructor;
    }

    public CountriesList fetchAllCountriesForSortedCountriesByDensity() {
        return fetchAllCountries(apiUrlConstructor.getDensityApiUrl());
    }

    public CountriesList fetchAllCountriesForAsianCountryWithMostBorderingDifferentRegion() {
        return fetchAllCountries(apiUrlConstructor.getBordersApiUrl());
    }

    private CountriesList fetchAllCountries(final String apiUrl) {
        Country[] countriesArray = restTemplate.getForObject(apiUrl, Country[].class);
        return CountriesList.of(Arrays.asList(countriesArray));
    }
}
