package com.countries.service;

import com.countries.model.Country;
import com.countries.model.CountryDensityDetail;
import com.countries.utils.exception.DataProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CountryService {
    public static final String API_URL = "https://restcountries.com/v3.1/all?fields=cca2,population,area";

    @Autowired
    private RestTemplate restTemplate;

    public List<CountryDensityDetail> getSortedCountriesByDensity() {
        Country[] countriesArray = restTemplate.getForObject(API_URL, Country[].class);
        List<Country> countriesData = Arrays.asList(countriesArray);

        return countriesData.stream()
                .map(country -> {
                    double density = 0;
                    double population = country.getPopulation();
                    double area = country.getArea();
                    density = population / area;
                    if (Double.isNaN(density) || Double.isInfinite(density)) {
                        throw new DataProcessingException("Error processing country data for " + country.getCca2());
                    }
                    return new CountryDensityDetail(country.getCca2(), density);
                })
                .sorted(Comparator.comparingDouble(CountryDensityDetail::getDensity).reversed())
                .toList();
    }
}
