package com.countries.service;

import com.countries.model.CountriesList;
import com.countries.model.Country;
import com.countries.model.dto.CountryDensityDetailDTO;
import com.countries.model.dto.CountryMostBordersDTO;
import com.countries.utils.exception.DataProcessingException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Float.NaN;

@Component
public class CountryTransformer {

    public CountryDensityDetailDTO toCountryDensityDetailDTO(Country country) {
        float population = country.getPopulation();
        float area = country.getArea();

        float density = area > 0 ? population / area : NaN;

        if (Float.isInfinite(density)) {
            throw new DataProcessingException("Error processing country data for " + country.getCca3());
        }

        return new CountryDensityDetailDTO(country.getCca3(), density);
    }

    public CountryMostBordersDTO mapToDTO(Country country, Set<String> nonAsianCountryCodes, CountriesList allCountries) {
        if (country == null) {
            return null;
        }

        Map<String, String> nonAsianBordersWithRegion = country.getBorders().stream()
                .filter(nonAsianCountryCodes::contains)
                .collect(Collectors.toMap(
                        border -> border,
                        border -> findCountryByCca3(border, allCountries).getRegion()
                ));

        return new CountryMostBordersDTO(country.getCca3(), country.getRegion(), nonAsianBordersWithRegion);
    }

    private Country findCountryByCca3(String cca3, CountriesList countries) {
        return countries.stream()
                .filter(country -> country.getCca3().equals(cca3))
                .findFirst()
                .orElse(null);
    }
}