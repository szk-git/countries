package unit.service;

import com.countries.model.CountriesList;
import com.countries.model.Country;
import com.countries.service.CountryDataService;
import com.countries.utils.ApiUrlConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CountryDataServiceUnitTest {

    @InjectMocks
    private CountryDataService countryDataService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ApiUrlConstructor apiUrlConstructor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest(name = "{index} - Test Fetch All Countries with mock URL: {1}")
    @MethodSource("densityApiDataProvider")
    void testFetchAllCountriesForSortedCountriesByDensity(Country[] mockCountries, String mockUrl, CountriesList expected) {
        when(apiUrlConstructor.getDensityApiUrl()).thenReturn(mockUrl);
        when(restTemplate.getForObject(mockUrl, Country[].class)).thenReturn(mockCountries);

        CountriesList result = countryDataService.fetchAllCountriesForSortedCountriesByDensity();

        assertEquals(expected, result);
    }

    @ParameterizedTest(name = "{index} - Test Fetch All Countries for Asian Country with mock URL: {1}")
    @MethodSource("bordersApiDataProvider")
    void testFetchAllCountriesForAsianCountryWithMostBorderingDifferentRegion(Country[] mockCountries, String mockUrl, CountriesList expected) {
        when(apiUrlConstructor.getBordersApiUrl()).thenReturn(mockUrl);
        when(restTemplate.getForObject(mockUrl, Country[].class)).thenReturn(mockCountries);

        CountriesList result = countryDataService.fetchAllCountriesForAsianCountryWithMostBorderingDifferentRegion();

        assertEquals(expected, result);
    }

    static Stream<Arguments> densityApiDataProvider() {
        return Stream.of(
                Arguments.of(
                        new Country[]{new Country("US", 1000, 10, "North America", null), new Country("UK", 500, 5, "Europe", null)},
                        "mockDensityApiUrl",
                        new CountriesList(Arrays.asList(new Country("US", 1000, 10, "North America", null), new Country("UK", 500, 5, "Europe", null)))
                )
        );
    }

    static Stream<Arguments> bordersApiDataProvider() {
        return Stream.of(
                Arguments.of(
                        new Country[]{new Country("CN", 1000, 10, "Asia", Arrays.asList("IN")), new Country("IN", 500, 5, "Asia", null)},
                        "mockBordersApiUrl",
                        new CountriesList(Arrays.asList(new Country("CN", 1000, 10, "Asia", Arrays.asList("IN")), new Country("IN", 500, 5, "Asia", null)))
                )
        );
    }
}
