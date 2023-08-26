package com.countries.model.response;

import com.countries.model.CountryDensityDetail;
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

    private List<CountryDensityDetail> countriesSortedByDensity;
    private OffsetDateTime timestamp;

    public CountryDensityResponse(List<CountryDensityDetail> countriesSortedByDensity) {
        this.countriesSortedByDensity = countriesSortedByDensity;
        this.timestamp = LocalDateTime.now().atOffset(ZoneOffset.UTC);
    }
}
