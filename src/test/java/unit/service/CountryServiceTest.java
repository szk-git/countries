package unit.service;

import com.countries.model.Country;
import com.countries.model.CountryDensityDetail;
import com.countries.service.CountryService;
import com.countries.utils.exception.DataProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.Float.NaN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CountryServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CountryService countryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
                ),
                Arguments.of(
                        new Country[]{new Country("BR", 5000, 100), new Country("JP", 1000, 5)},
                        List.of(new CountryDensityDetail("JP", 200), new CountryDensityDetail("BR", 50))
                ),
                Arguments.of(
                        new Country[]{new Country("ZA", 2000, 1), new Country("AU", 2000, 40)},
                        List.of(new CountryDensityDetail("ZA", 2000), new CountryDensityDetail("AU", 50))
                ),
                Arguments.of(
                        new Country[]{new Country("HU", -1, 1), new Country("AU", 2000, 40)},
                        List.of(new CountryDensityDetail("HU", NaN), new CountryDensityDetail("AU", 50))
                )
        );
    }


    @ParameterizedTest(name = "{index}st test for CountriesByDensitySuccess")
    @MethodSource("countryDataProviderForSuccess")
    void testGetSortedCountriesByDensitySuccess(Country[] countriesArray, List<CountryDensityDetail> expected) {
        when(restTemplate.getForObject(CountryService.API_URL, Country[].class)).thenReturn(countriesArray);

        List<CountryDensityDetail> result = countryService.getSortedCountriesByDensity();

        verify(restTemplate).getForObject(CountryService.API_URL, Country[].class);

        assertEquals(expected, result);
    }

    @Test
    void testGetSortedCountriesByDensity_APIReturnsNull_ShouldThrowNullPointerException() {
        when(restTemplate.getForObject(CountryService.API_URL, Country[].class)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> countryService.getSortedCountriesByDensity());

        verify(restTemplate).getForObject(CountryService.API_URL, Country[].class);
    }

    @Test
    void testGetSortedCountriesByDensity_DataProcessingError_ShouldThrowDataProcessingException() {
        Country countryWithNullArea = new Country("US", 0, 0);  // This country should trigger the exception
        Country[] countriesArray = new Country[]{countryWithNullArea};

        when(restTemplate.getForObject(CountryService.API_URL, Country[].class)).thenReturn(countriesArray);

        Exception exception = assertThrows(DataProcessingException.class,
                () -> countryService.getSortedCountriesByDensity());

        assertTrue(exception.getMessage().contains("Error processing country data for US"));

        verify(restTemplate).getForObject(CountryService.API_URL, Country[].class);
    }

    @Test
    void testGetSortedCountriesByDensity_RestClientException_ShouldPropagateException() {
        when(restTemplate.getForObject(CountryService.API_URL, Country[].class))
                .thenThrow(new RestClientException("API failed"));

        assertThrows(RestClientException.class, () -> countryService.getSortedCountriesByDensity());

        verify(restTemplate).getForObject(CountryService.API_URL, Country[].class);
    }
}
