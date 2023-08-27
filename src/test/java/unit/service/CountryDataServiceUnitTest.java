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
                        "mockDensityApiUrl1",
                        new CountriesList(Arrays.asList(new Country("US", 1000, 10, "North America", null), new Country("UK", 500, 5, "Europe", null)))
                ),
                Arguments.of(
                        new Country[]{new Country("CN", 1400, 14, "Asia", null), new Country("IN", 1300, 3, "Asia", null)},
                        "mockDensityApiUrl2",
                        new CountriesList(Arrays.asList(new Country("CN", 1400, 14, "Asia", null), new Country("IN", 1300, 3, "Asia", null)))
                ),
                Arguments.of(
                        new Country[]{new Country("FR", 67, 1, "Europe", null), new Country("DE", 83, 2, "Europe", null)},
                        "mockDensityApiUrl3",
                        new CountriesList(Arrays.asList(new Country("FR", 67, 1, "Europe", null), new Country("DE", 83, 2, "Europe", null)))
                ),
                Arguments.of(
                        new Country[]{new Country("BR", 210, 9, "South America", null), new Country("AR", 44, 3, "South America", null)},
                        "mockDensityApiUrl4",
                        new CountriesList(Arrays.asList(new Country("BR", 210, 9, "South America", null), new Country("AR", 44, 3, "South America", null)))
                ),
                Arguments.of(
                        new Country[]{new Country("AU", 25, 8, "Oceania", null), new Country("NZ", 5, 1, "Oceania", null)},
                        "mockDensityApiUrl5",
                        new CountriesList(Arrays.asList(new Country("AU", 25, 8, "Oceania", null), new Country("NZ", 5, 1, "Oceania", null)))
                )
        );
    }

    static Stream<Arguments> bordersApiDataProvider() {
        return Stream.of(
                Arguments.of(
                        new Country[]{new Country("CN", 1000, 10, "Asia", Arrays.asList("IN")), new Country("IN", 500, 5, "Asia", null)},
                        "mockBordersApiUrl1",
                        new CountriesList(Arrays.asList(new Country("CN", 1000, 10, "Asia", Arrays.asList("IN")), new Country("IN", 500, 5, "Asia", null)))
                ),
                Arguments.of(
                        new Country[]{new Country("JP", 800, 7, "Asia", Arrays.asList("KR")), new Country("KR", 600, 6, "Asia", null)},
                        "mockBordersApiUrl2",
                        new CountriesList(Arrays.asList(new Country("JP", 800, 7, "Asia", Arrays.asList("KR")), new Country("KR", 600, 6, "Asia", null)))
                ),
                Arguments.of(
                        new Country[]{new Country("RU", 1500, 20, "Asia", Arrays.asList("CN", "MN")), new Country("MN", 400, 5, "Asia", null)},
                        "mockBordersApiUrl3",
                        new CountriesList(Arrays.asList(new Country("RU", 1500, 20, "Asia", Arrays.asList("CN", "MN")), new Country("MN", 400, 5, "Asia", null)))
                ),
                Arguments.of(
                        new Country[]{new Country("TH", 700, 8, "Asia", Arrays.asList("LA", "MM")), new Country("MM", 550, 6, "Asia", null)},
                        "mockBordersApiUrl4",
                        new CountriesList(Arrays.asList(new Country("TH", 700, 8, "Asia", Arrays.asList("LA", "MM")), new Country("MM", 550, 6, "Asia", null)))
                ),
                Arguments.of(
                        new Country[]{new Country("ID", 1300, 15, "Asia", Arrays.asList("MY")), new Country("MY", 650, 7, "Asia", null)},
                        "mockBordersApiUrl5",
                        new CountriesList(Arrays.asList(new Country("ID", 1300, 15, "Asia", Arrays.asList("MY")), new Country("MY", 650, 7, "Asia", null)))
                )
        );
    }
}
