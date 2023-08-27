package com.countries.model.response;

import com.countries.model.dto.CountryMostBordersDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryMostBordersResponse {

    private CountryMostBordersDTO countryByMostBordersFromAnotherRegion;
    private OffsetDateTime timestamp;

    public CountryMostBordersResponse(CountryMostBordersDTO countryByMostBordersFromAnotherRegion) {
        this.countryByMostBordersFromAnotherRegion = countryByMostBordersFromAnotherRegion;
        this.timestamp = LocalDateTime.now().atOffset(ZoneOffset.UTC);
    }
}
