package integration.controller;

import com.countries.CountriesApplication;
import com.countries.model.dto.CountryDensityDetailDTO;
import com.countries.model.dto.CountryMostBordersDTO;
import com.countries.model.response.CountryDensityResponse;
import com.countries.model.response.CountryMostBordersResponse;
import com.countries.service.CountryAnalyzer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest(classes = CountriesApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CountryControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private CountryAnalyzer countryAnalyzer;

    static Stream<Arguments> sortedCountriesByDensityProvider() {
        return Stream.of(
                Arguments.of(
                        List.of(new CountryDensityDetailDTO("US", 100), new CountryDensityDetailDTO("UK", 90)),
                        new CountryDensityResponse(List.of(new CountryDensityDetailDTO("US", 100), new CountryDensityDetailDTO("UK", 90)))
                )
        );
    }

    static Stream<Arguments> asianCountryWithMostBorderingDifferentRegionProvider() {
        return Stream.of(
                Arguments.of(
                        new CountryMostBordersDTO("CN", "Asia", Map.of("RU", "Europe")),
                        new CountryMostBordersResponse(new CountryMostBordersDTO("CN", "Asia", Map.of("RU", "Europe")))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("sortedCountriesByDensityProvider")
    void testGetSortedCountriesByDensity(List<CountryDensityDetailDTO> mockData, CountryDensityResponse expectedResponse) {
        when(countryAnalyzer.getSortedCountriesByDensity()).thenReturn(mockData);

        ResponseEntity<CountryDensityResponse> response = testRestTemplate.getForEntity("/api/v1/countries/sortedByDensity", CountryDensityResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("timestamp").isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @MethodSource("asianCountryWithMostBorderingDifferentRegionProvider")
    void testGetAsianCountryWithMostBorderingDifferentRegion(CountryMostBordersDTO mockData, CountryMostBordersResponse expectedResponse) {
        when(countryAnalyzer.getAsianCountryWithMostBorderingDifferentRegion()).thenReturn(mockData);

        ResponseEntity<CountryMostBordersResponse> response = testRestTemplate.getForEntity("/api/v1/countries/asiaMaxBorderingDifferentRegion", CountryMostBordersResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("timestamp").isEqualTo(expectedResponse);
    }
}