package com.countries.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    private String cca3;
    private float population;
    private float area;
    private String region;
    private List<String> borders;
}
