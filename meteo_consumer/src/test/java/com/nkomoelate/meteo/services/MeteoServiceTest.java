package com.nkomoelate.meteo.service;

import com.nkomoelate.meteo.client.GeoCodingClient;
import com.nkomoelate.meteo.client.WeatherClient;
import com.nkomoelate.meteo.client.dto.DailyWeather;
import com.nkomoelate.meteo.client.dto.GeoCodingResult;
import com.nkomoelate.meteo.models.EnumSkyWeather;
import com.nkomoelate.meteo.models.Weather;
import com.nkomoelate.meteo.services.MeteoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeteoServiceTest {

    @Mock
    private GeoCodingClient geoCodingClient;

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private MeteoService meteoService;

    @Test
    void getWeatherByCity_retourneUnJourParDateAvecLeNomOfficiel() {
        // Given
        GeoCodingResult rennes = new GeoCodingResult(
                "Rennes", 48.11, -1.68, "France",
                "Europe/Paris", "Bretagne", "Ille-et-Vilaine", "Rennes");
        DailyWeather daily = new DailyWeather(
                List.of(LocalDate.of(2026, 10, 12), LocalDate.of(2026, 10, 13)),
                List.of(19.2, 20.4),
                List.of(13.6, 14.0),
                List.of(4.5, 0.7),
                List.of(15.9, 17.6),
                List.of(61, 3)
        );
        when(geoCodingClient.findCity("rennes")).thenReturn(rennes);
        when(weatherClient.getDailyForeCast(48.11, -1.68)).thenReturn(daily);

        // When
        List<Weather> result = meteoService.getWeatherByCity("rennes");

        // Then
        assertThat(result).hasSize(2);
        Weather premierJour = result.get(0);
        assertThat(premierJour.getCityName()).isEqualTo("Rennes");
        assertThat(premierJour.getDate()).isEqualTo(LocalDate.of(2026, 10, 12));
        assertThat(premierJour.getTemperatureMax()).isEqualTo(19.2);
        assertThat(premierJour.getCondition()).isEqualTo(EnumSkyWeather.RAIN);
        assertThat(result.get(1).getCondition()).isEqualTo(EnumSkyWeather.CLOUDY);
    }
}
