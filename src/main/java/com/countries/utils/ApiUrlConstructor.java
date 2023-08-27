package com.countries.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiUrlConstructor {

    private String apiBase;
    private String densityFields;
    private String bordersFields;

    @Autowired
    public ApiUrlConstructor(
            @Value("${countries.api.base}") String apiBase,
            @Value("${countries.api.density.fields}") String densityFields,
            @Value("${countries.api.borders.fields}") String bordersFields) {
        this.apiBase = apiBase;
        this.densityFields = densityFields;
        this.bordersFields = bordersFields;
    }

    public String getDensityApiUrl() {
        return constructUrl(densityFields);
    }

    public String getBordersApiUrl() {
        return constructUrl(bordersFields);
    }

    private String constructUrl(String fields) {
        return apiBase + "all?fields=" + fields;
    }
}
