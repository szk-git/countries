package unit.service;

import com.countries.model.CountriesList;
import com.countries.model.Country;
import com.countries.model.dto.CountryDensityDetailDTO;
import com.countries.model.dto.CountryMostBordersDTO;
import com.countries.service.CountryAnalyzerService;
import com.countries.service.CountryDataService;
import com.countries.service.CountryTransformerComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CountryAnalyzerServiceUnitTest {

    @InjectMocks
    private CountryAnalyzerService countryAnalyzerService;

    @Mock
    private CountryDataService countryDataService;

    @Mock
    private CountryTransformerComponent countryTransformerComponent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest(name = "{index} - Test Sorted Countries By Density with mock countries: {0}")
    @MethodSource("densityDataProvider")
    void testGetSortedCountriesByDensity(CountriesList mockCountries, List<CountryDensityDetailDTO> expected) {
        when(countryDataService.fetchAllCountriesForSortedCountriesByDensity()).thenReturn(mockCountries);
        mockCountries.getCountries().forEach(country -> {
            when(countryTransformerComponent.toCountryDensityDetailDTO(country)).thenReturn(new CountryDensityDetailDTO(country.getCca3(), country.getPopulation() / country.getArea()));
        });

        List<CountryDensityDetailDTO> result = countryAnalyzerService.getSortedCountriesByDensity();

        assertEquals(expected, result);
    }

    @ParameterizedTest(name = "{index} - Test Asian Country with Most Bordering Different Region with mock countries: {0}")
    @MethodSource("borderDataProvider")
    void testGetAsianCountryWithMostBorderingDifferentRegion(CountriesList mockCountries, CountryMostBordersDTO expected) {
        when(countryDataService.fetchAllCountriesForAsianCountryWithMostBorderingDifferentRegion()).thenReturn(mockCountries);
        mockCountries.getCountries().forEach(country -> {
            Set<String> nonAsianCountries = mockCountries.getCountries().stream().filter(c -> !"Asia".equalsIgnoreCase(c.getRegion())).map(Country::getCca3).collect(Collectors.toSet());
            when(countryTransformerComponent.mapToDTO(country, nonAsianCountries, mockCountries)).thenReturn(
                    new CountryMostBordersDTO(country.getCca3(), country.getRegion(), country.getBorders().stream().filter(nonAsianCountries::contains).collect(Collectors.toMap(border -> border, border -> {
                        Country borderCountry = mockCountries.getCountries().stream().filter(c -> c.getCca3().equals(border)).findFirst().orElse(null);
                        return borderCountry != null ? borderCountry.getRegion() : null;
                    })))
            );
        });

        CountryMostBordersDTO result = countryAnalyzerService.getAsianCountryWithMostBorderingDifferentRegion();

        assertEquals(expected, result);
    }

    static Stream<Arguments> densityDataProvider() {
        return Stream.of(
                Arguments.of(
                        new CountriesList(List.of(
                                new Country("US", 1000, 10, "North America", null),
                                new Country("UK", 500, 5, "Europe", null)
                        )),
                        List.of(
                                new CountryDensityDetailDTO("US", 100),
                                new CountryDensityDetailDTO("UK", 100)
                        )
                ),
                Arguments.of(
                        new CountriesList(List.of(
                                new Country("CN", 1400, 14, "Asia", null),
                                new Country("IN", 1200, 3, "Asia", null)
                        )),
                        List.of(
                                new CountryDensityDetailDTO("IN", 400),
                                new CountryDensityDetailDTO("CN", 100)
                        )
                ),
                Arguments.of(
                        new CountriesList(List.of(
                                new Country("FR", 64, 1, "Europe", null),
                                new Country("DE", 84, 1, "Europe", null)
                        )),
                        List.of(
                                new CountryDensityDetailDTO("DE", 84),
                                new CountryDensityDetailDTO("FR", 64)
                        )
                ),
                Arguments.of(
                        new CountriesList(List.of(
                                new Country("BR", 210, 7, "South America", null),
                                new Country("AR", 42, 3, "South America", null)
                        )),
                        List.of(
                                new CountryDensityDetailDTO("BR", 30),
                                new CountryDensityDetailDTO("AR", 14)
                        )
                ),
                Arguments.of(
                        new CountriesList(List.of(
                                new Country("AU", 24, 8, "Oceania", null),
                                new Country("NZ", 5, 1, "Oceania", null)
                        )),
                        List.of(
                                new CountryDensityDetailDTO("NZ", 5),
                                new CountryDensityDetailDTO("AU", 3)
                        )
                )
        );
    }

    static Stream<Arguments> borderDataProvider() {
        return Stream.of(
                Arguments.of(
                        new CountriesList(List.of(
                                new Country("CN", 1000, 10, "Asia", List.of("IN", "RU")),
                                new Country("IN", 500, 5, "Asia", List.of("CN")),
                                new Country("RU", 800, 20, "Europe", List.of("CN"))
                        )),
                        new CountryMostBordersDTO("CN", "Asia", Map.of("RU", "Europe"))
                ),
                Arguments.of(
                        new CountriesList(List.of(
                                new Country("IN", 1300, 3.2f, "Asia", List.of("PAK", "CN")),
                                new Country("PAK", 220, 0.881f, "Africa", List.of("IN")),
                                new Country("CN", 1400, 14, "Asia", List.of("IN"))
                        )),
                        new CountryMostBordersDTO("IN", "Asia", Map.of("PAK", "Africa"))
                ),
                Arguments.of(
                        new CountriesList(List.of(
                                new Country("JP", 126, 0.377f, "Asia", List.of()),
                                new Country("CN", 1400, 14, "Asia", List.of("JP"))
                        )),
                        null
                ),
                Arguments.of(
                        new CountriesList(List.of(
                                new Country("TH", 69, 0.513f, "Asia", List.of("MY", "MM")),
                                new Country("MY", 32, 0.330f, "Africa", List.of("TH")),
                                new Country("MM", 54, 0.676f, "Asia", List.of("TH"))
                        )),
                        new CountryMostBordersDTO("TH", "Asia", Map.of("MY", "Africa"))
                ),
                Arguments.of(
                        new CountriesList(List.of(
                                new Country("MN", 3, 1.566f, "Asia", List.of("CN", "RU")),
                                new Country("RU", 144, 17.1f, "Europe", List.of("MN")),
                                new Country("CN", 1400, 14, "Asia", List.of("MN"))
                        )),
                        new CountryMostBordersDTO("MN", "Asia", Map.of("RU", "Europe"))
                )
        );
    }
}
