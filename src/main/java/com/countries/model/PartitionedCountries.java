package com.countries.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartitionedCountries {
    private CountriesList asianCountries;
    private CountriesList nonAsianCountries;

    public static PartitionedCountries of(final CountriesList asianCountries, final CountriesList nonAsianCountries) {
        return new PartitionedCountries(asianCountries, nonAsianCountries);
    }
}
