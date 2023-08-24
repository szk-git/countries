package com.countries.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CountryControllerTest {

    @InjectMocks
    private CountryController countryController;

    @Mock
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
    }

    @ParameterizedTest
    @CsvSource({
            "/api/v1/countries/sorted-by-density, [\"SORTED BY DENSITY\"]",
            "/api/v1/countries/asia-max-bordering-different-region, ASIA MAX BORDERING DIFFERENT REGION"
    })
    void testEndpoints(String endpoint, String expectedResponse) throws Exception {
        mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}
