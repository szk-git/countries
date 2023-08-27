package unit.utils;

import com.countries.utils.ApiUrlConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiUrlConstructorUnitTest {

    private ApiUrlConstructor apiUrlConstructor;

    private final String mockApiBase = "http://api.example.com/";
    private final String mockDensityFields = "name,density";
    private final String mockBordersFields = "name,borders";

    @BeforeEach
    public void setUp() {
        apiUrlConstructor = new ApiUrlConstructor(mockApiBase, mockDensityFields, mockBordersFields);
    }

    @Test
    void testGetDensityApiUrl() {
        String expectedUrl = mockApiBase + "all?fields=" + mockDensityFields;
        String actualUrl = apiUrlConstructor.getDensityApiUrl();
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void testGetBordersApiUrl() {
        String expectedUrl = mockApiBase + "all?fields=" + mockBordersFields;
        String actualUrl = apiUrlConstructor.getBordersApiUrl();
        assertEquals(expectedUrl, actualUrl);
    }
}
