package com.nkomoelate.meteo.services;

import com.nkomoelate.meteo.client.GeoCodingClient;
import com.nkomoelate.meteo.client.WeatherClient;
import com.nkomoelate.meteo.client.dto.DailyWeather;
import com.nkomoelate.meteo.client.dto.GeoCodingResult;
import com.nkomoelate.meteo.exceptions.CityNotFoundException;
import com.nkomoelate.meteo.exceptions.WeatherProviderException;
import com.nkomoelate.meteo.models.EnumSkyWeather;
import com.nkomoelate.meteo.models.Weather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeteoServiceTest {

    @Mock
    private GeoCodingClient geoCodingClient;

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private MeteoService meteoService;

    private static final GeoCodingResult RENNES = new GeoCodingResult(
            "Rennes", 48.11, -1.68, "France",
            "Europe/Paris", "Bretagne", "Ille-et-Vilaine", "Rennes");

    @Test
    void getWeatherByCity_retourneUnJourParDateAvecLeNomOfficiel() {
        // Given
        DailyWeather daily = new DailyWeather(
                List.of(LocalDate.of(2026, 10, 12), LocalDate.of(2026, 10, 13)),
                List.of(19.2, 20.4),
                List.of(13.6, 14.0),
                List.of(4.5, 0.7),
                List.of(15.9, 17.6),
                List.of(61, 3)
        );
        when(geoCodingClient.findCity("rennes")).thenReturn(RENNES);
        when(weatherClient.getDailyForeCast(48.11, -1.68)).thenReturn(daily);

        // When
        List<Weather> result = meteoService.getWeatherByCity("rennes");

        // Then
        assertThat(result).hasSize(2);
        Weather premierJour = result.get(0);
        assertThat(premierJour.getCityName()).isEqualTo("Rennes");
        assertThat(premierJour.getDate()).isEqualTo(LocalDate.of(2026, 10, 12));
        assertThat(premierJour.getTemperatureMax()).isEqualTo(19.2);
        assertThat(premierJour.getTemperatureMin()).isEqualTo(13.6);
        assertThat(premierJour.getPrecipitation()).isEqualTo(4.5);
        assertThat(premierJour.getWindSpeed()).isEqualTo(15.9);
        assertThat(premierJour.getCondition()).isEqualTo(EnumSkyWeather.RAIN);
        assertThat(result.get(1).getCondition()).isEqualTo(EnumSkyWeather.CLOUDY);
    }

    @Test
    void getWeatherByCity_villeIntrouvable_laisseRemonterLExceptionSansAppelerLaMeteo() {
        // Given
        when(geoCodingClient.findCity("xyzabc")).thenThrow(new CityNotFoundException("xyzabc"));

        // When / Then
        assertThatThrownBy(() -> meteoService.getWeatherByCity("xyzabc"))
                .isInstanceOf(CityNotFoundException.class)
                .hasMessageContaining("xyzabc");
        verifyNoInteractions(weatherClient);
    }

    @Test
    void getWeatherByCity_apiMeteoIndisponible_laisseRemonterLException() {
        // Given
        when(geoCodingClient.findCity("rennes")).thenReturn(RENNES);
        when(weatherClient.getDailyForeCast(48.11, -1.68))
                .thenThrow(new WeatherProviderException("API météo indisponible"));

        // When / Then
        assertThatThrownBy(() -> meteoService.getWeatherByCity("rennes"))
                .isInstanceOf(WeatherProviderException.class)
                .hasMessage("API météo indisponible");
    }
}