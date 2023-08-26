package integration.controller;

import com.countries.CountriesApplication;
import com.countries.model.Country;
import com.countries.model.CountryDensityDetail;
import com.countries.model.response.CountryDensityResponse;
import com.countries.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(classes = CountriesApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CountryControllerIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    static Stream<Arguments> countryDataProviderForSuccess() {
        return Stream.of(
                Arguments.of(
                        new Country[]{new Country("US", 1000, 10), new Country("UK", 500, 5)},
                        List.of(new CountryDensityDetail("US", 100), new CountryDensityDetail("UK", 100))
                ),
                Arguments.of(
                        new Country[]{new Country("CA", 1200, 12), new Country("FR", 600, 3)},
                        List.of(new CountryDensityDetail("FR", 200), new CountryDensityDetail("CA", 100))
                )
        );
    }

    @ParameterizedTest(name = "{index} - Test Countries By Density with countries: {0}")
    @MethodSource("countryDataProviderForSuccess")
    void testGetSortedCountriesByDensity(Country[] mockCountries, List<CountryDensityDetail> expected) throws Exception {
        String mockCountriesJson = objectMapper.writeValueAsString(mockCountries); // Convert to JSON

        mockServer.expect(requestTo(CountryService.API_URL))
                .andRespond(withSuccess(mockCountriesJson, MediaType.APPLICATION_JSON));

        ResponseEntity<CountryDensityResponse> response = testRestTemplate.getForEntity("/api/v1/countries/sortedByDensity", CountryDensityResponse.class);

        List<CountryDensityDetail> actualDetails = Objects.requireNonNull(response.getBody()).getCountriesSortedByDensity();

        assertEquals(expected, actualDetails);
    }
}
