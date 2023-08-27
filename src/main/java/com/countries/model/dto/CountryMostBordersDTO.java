package com.countries.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryMostBordersDTO {
    private String cca3;
    private String region;
    private Map<String, String> bordersWithRegion;
}
