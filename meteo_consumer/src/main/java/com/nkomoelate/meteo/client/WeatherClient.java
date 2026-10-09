package com.nkomoelate.meteo.client;

import com.nkomoelate.meteo.client.dto.DailyWeather;
import com.nkomoelate.meteo.client.dto.WeatherResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WeatherClient {

    private final RestClient restClient;

    public WeatherClient(){
        restClient =  RestClient.builder()
                .baseUrl("https://api.open-meteo.com")
                .build();
    }
    public DailyWeather getDailyForeCast(double latitude, double longitude){

        WeatherResponse response =  restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("timezone", "auto")
                        .queryParam("daily","temperature_2m_max,temperature_2m_min,precipitation_sum,wind_speed_10m_max,weather_code")
                        .build())
                .retrieve()
                .body(WeatherResponse.class);

        if (response == null || response.daily() == null) {
            throw new IllegalStateException("daily introuvable");
        }
        return response.daily();
    }
}
