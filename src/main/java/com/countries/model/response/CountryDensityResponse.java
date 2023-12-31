package com.countries.model.response;

import com.countries.model.dto.CountryDensityDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDensityResponse {

    private List<CountryDensityDetailDTO> countriesSortedByDensity;
    private OffsetDateTime timestamp;

    public CountryDensityResponse(List<CountryDensityDetailDTO> countriesSortedByDensity) {
        this.countriesSortedByDensity = countriesSortedByDensity;
        this.timestamp = LocalDateTime.now().atOffset(ZoneOffset.UTC);
    }
}
