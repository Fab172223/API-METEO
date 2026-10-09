package com.nkomoelate.meteo.client;

import com.nkomoelate.meteo.client.dto.GeoCodingResponse;
import com.nkomoelate.meteo.client.dto.GeoCodingResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GeoCodingClient {
    private final RestClient restClient;

    public GeoCodingClient(){
        restClient = RestClient.builder()
                .baseUrl("https://geocoding-api.open-meteo.com")
                .build();
    }

    public GeoCodingResult findCity(String city){
        GeoCodingResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search")
                        .queryParam("name", city)
                        .queryParam("count", 1)
                        .queryParam("language", "fr")
                        .build())
                .retrieve()
                .body(GeoCodingResponse.class);

        if (response == null || response.results() == null || response.results().isEmpty()) {
            throw new IllegalArgumentException("Ville introuvable : " + city);
        }
        return response.results().getFirst();
    }
}
