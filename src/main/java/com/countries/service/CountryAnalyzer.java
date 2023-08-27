package com.countries.service;

import com.countries.model.CountriesList;
import com.countries.model.Country;
import com.countries.model.PartitionedCountries;
import com.countries.model.dto.CountryDensityDetailDTO;
import com.countries.model.dto.CountryMostBordersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CountryAnalyzer {

    private final CountryDataService countryDataService;
    private final CountryTransformer countryTransformer;

    @Autowired
    public CountryAnalyzer(CountryDataService countryDataService, CountryTransformer countryTransformer) {
        this.countryDataService = countryDataService;
        this.countryTransformer = countryTransformer;
    }

    public List<CountryDensityDetailDTO> getSortedCountriesByDensity() {
        CountriesList countriesData = countryDataService.fetchAllCountriesForSortedCountriesByDensity();
        return countriesData.stream()
                .map(countryTransformer::toCountryDensityDetailDTO)
                .sorted(Comparator.comparingDouble(CountryDensityDetailDTO::getPopulationDensity).reversed())
                .toList();
    }

    public CountryMostBordersDTO getAsianCountryWithMostBorderingDifferentRegion() {
        CountriesList allCountriesList = countryDataService.fetchAllCountriesForAsianCountryWithMostBorderingDifferentRegion();
        PartitionedCountries partitionedCountries = partitionCountriesByRegion(allCountriesList);
        Set<String> nonAsianCountryCodes = extractNonAsianCountryCodes(partitionedCountries);

        return findAsianCountryWithMostNonAsianBorders(partitionedCountries.getAsianCountries(), nonAsianCountryCodes, allCountriesList);
    }

    private PartitionedCountries partitionCountriesByRegion(CountriesList countriesList) {
        Map<Boolean, List<Country>> partitionedMap = countriesList.stream()
                .collect(Collectors.partitioningBy(country -> "Asia".equalsIgnoreCase(country.getRegion())));
        return PartitionedCountries.of(CountriesList.of(partitionedMap.get(true)), CountriesList.of(partitionedMap.get(false)));
    }

    private Set<String> extractNonAsianCountryCodes(PartitionedCountries partitionedCountries) {
        return partitionedCountries.getNonAsianCountries().stream()
                .map(Country::getCca3)
                .collect(Collectors.toSet());
    }

    private CountryMostBordersDTO findAsianCountryWithMostNonAsianBorders(CountriesList asianCountries, Set<String> nonAsianCountryCodes, CountriesList allCountries) {
        Country winner = asianCountries.stream()
                .max(Comparator.comparingInt(country -> countNonAsianBorders(country, nonAsianCountryCodes)))
                .orElse(null);

        CountryMostBordersDTO resultDTO = countryTransformer.mapToDTO(winner, nonAsianCountryCodes, allCountries);

        if (resultDTO.getBordersWithRegion().isEmpty()) {
            return null;
        }

        return resultDTO;
    }

    private int countNonAsianBorders(Country country, Set<String> nonAsianCountryCodes) {
        return (int) country.getBorders().stream().filter(nonAsianCountryCodes::contains).count();
    }
}
