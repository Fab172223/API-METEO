package com.nkomoelate.meteo.client;

import com.nkomoelate.meteo.client.dto.DailyWeather;
import com.nkomoelate.meteo.client.dto.WeatherResponse;
import com.nkomoelate.meteo.exceptions.WeatherProviderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class WeatherClient {

    private final RestClient restClient;

    public WeatherClient(@Value("${openmeteo.forecast.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
    public DailyWeather getDailyForeCast(double latitude, double longitude) {
        WeatherResponse response;
        try{
             response =  restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/forecast")
                            .queryParam("latitude", latitude)
                            .queryParam("longitude", longitude)
                            .queryParam("timezone", "auto")
                            .queryParam("daily","temperature_2m_max,temperature_2m_min,precipitation_sum,wind_speed_10m_max,weather_code")
                            .build())
                    .retrieve()
                    .body(WeatherResponse.class);
            } catch (RestClientException restClientException) {
                throw new WeatherProviderException("API de géocodage indisponible", restClientException);
            }
                if (response == null || response.daily() == null) {
                    throw new WeatherProviderException("Réponse de l'API météo invalide");
                }
                return response.daily();
    }
}
