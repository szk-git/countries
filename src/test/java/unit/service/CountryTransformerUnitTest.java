package unit.service;

import com.countries.model.Country;
import com.countries.model.CountriesList;
import com.countries.model.dto.CountryDensityDetailDTO;
import com.countries.model.dto.CountryMostBordersDTO;
import com.countries.service.CountryTransformer;
import com.countries.utils.exception.DataProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CountryTransformerUnitTest {

    private CountryTransformer transformer;

    @BeforeEach
    void setUp() {
        transformer = new CountryTransformer();
    }

    @ParameterizedTest(name = "{index} - Testing density calculation for country: {0}")
    @MethodSource("countryDensityProvider")
    void testToCountryDensityDetailDTO(Country country, float expectedDensity) {
        if (Float.isInfinite(country.getPopulation())) {
            assertThrows(DataProcessingException.class, () -> transformer.toCountryDensityDetailDTO(country));
            return;
        }

        CountryDensityDetailDTO dto = transformer.toCountryDensityDetailDTO(country);
        assertEquals(country.getCca3(), dto.getCca2());
        assertEquals(expectedDensity, dto.getPopulationDensity());
    }

    @ParameterizedTest(name = "{index} - Testing borders for country: {0}")
    @MethodSource("countryBordersProvider")
    void testMapToDTO(Country country, HashSet<String> nonAsianCountryCodes, CountriesList allCountries, HashSet<String> expectedBorders) {
        CountryMostBordersDTO dto = transformer.mapToDTO(country, nonAsianCountryCodes, allCountries);
        assertEquals(expectedBorders, dto.getBordersWithRegion().keySet());
    }

    static Stream<Arguments> countryDensityProvider() {
        return Stream.of(
                Arguments.of(new Country("CCA", 100, 10, "Asia", null), 10.0f),
                Arguments.of(new Country("CCA", 100, 0, "Asia", null), Float.NaN),
                Arguments.of(new Country("CCA", Float.POSITIVE_INFINITY, 1, "Asia", null), Float.NaN)
        );
    }

    static Stream<Arguments> countryBordersProvider() {
        return Stream.of(
                Arguments.of(
                        new Country("IND", 1000, 2000, "Asia", Arrays.asList("PAK", "CHN", "NPL")),
                        new HashSet<>(List.of("PAK")),
                        new CountriesList(Arrays.asList(
                                new Country("IND", 1000, 2000, "Asia", Arrays.asList("PAK", "CHN", "NPL")),
                                new Country("PAK", 200, 300, "Africa", null),
                                new Country("CHN", 300, 400, "Asia", null)
                        )),
                        new HashSet<>(List.of("PAK"))
                ),
                Arguments.of(
                        new Country("IND", 1000, 2000, "Asia", Arrays.asList("PAK", "CHN", "NPL")),
                        new HashSet<>(List.of("AFG")),
                        new CountriesList(Arrays.asList(
                                new Country("IND", 1000, 2000, "Asia", Arrays.asList("PAK", "CHN", "NPL")),
                                new Country("PAK", 200, 300, "Asia", null),
                                new Country("CHN", 300, 400, "Asia", null)
                        )),
                        new HashSet<>()
                )
        );
    }
}
