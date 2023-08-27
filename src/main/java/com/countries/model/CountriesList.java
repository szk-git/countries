package com.countries.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountriesList {
    private List<Country> countries;

    public static CountriesList of(final List<Country> countries) {
        return new CountriesList(countries);
    }

    public Stream<Country> stream() {
        return countries.stream();
    }
}
