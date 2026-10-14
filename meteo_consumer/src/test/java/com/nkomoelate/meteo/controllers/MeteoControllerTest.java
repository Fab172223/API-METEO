package com.nkomoelate.meteo.controllers;

import com.nkomoelate.meteo.exceptions.CityNotFoundException;
import com.nkomoelate.meteo.exceptions.WeatherProviderException;
import com.nkomoelate.meteo.models.EnumSkyWeather;
import com.nkomoelate.meteo.models.Weather;
import com.nkomoelate.meteo.services.MeteoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeteoController.class)
class MeteoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeteoService meteoService;

    @Test
    void getMeteo_villeConnue_retourne200EtLesPrevisions() throws Exception {
        Weather jour = new Weather();
        jour.setCityName("Rennes");
        jour.setDate(LocalDate.of(2026, 10, 12));
        jour.setTemperatureMax(19.2);
        jour.setTemperatureMin(13.6);
        jour.setPrecipitation(4.5);
        jour.setWindSpeed(15.9);
        jour.setCondition(EnumSkyWeather.RAIN);
        when(meteoService.getWeatherByCity("Rennes")).thenReturn(List.of(jour));

        mockMvc.perform(get("/api/meteo").param("city", "Rennes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cityName").value("Rennes"))
                .andExpect(jsonPath("$[0].date").value("2026-10-12"))
                .andExpect(jsonPath("$[0].temperatureMax").value(19.2))
                .andExpect(jsonPath("$[0].condition").value("RAIN"));
    }

    @Test
    void getMeteo_villeIntrouvable_retourne404() throws Exception {
        when(meteoService.getWeatherByCity("xyzabc")).thenThrow(new CityNotFoundException("xyzabc"));

        mockMvc.perform(get("/api/meteo").param("city", "xyzabc"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("xyzabc: Ville non trouvée"));
    }

    @Test
    void getMeteo_apiIndisponible_retourne502() throws Exception {
        when(meteoService.getWeatherByCity("Rennes"))
                .thenThrow(new WeatherProviderException("API météo indisponible"));

        mockMvc.perform(get("/api/meteo").param("city", "Rennes"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502))
                .andExpect(jsonPath("$.detail").value("API météo indisponible"));
    }

    @Test
    void getMeteo_sansParametreCity_retourne400() throws Exception {
        mockMvc.perform(get("/api/meteo"))
                .andExpect(status().isBadRequest());
    }
}