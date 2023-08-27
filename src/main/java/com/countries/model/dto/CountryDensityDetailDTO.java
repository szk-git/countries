package com.countries.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDensityDetailDTO {
    private String cca2;
    private float populationDensity;
}