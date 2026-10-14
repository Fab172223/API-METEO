package com.nkomoelate.meteo.client;

import com.nkomoelate.meteo.client.dto.GeoCodingResponse;
import com.nkomoelate.meteo.client.dto.GeoCodingResult;
import com.nkomoelate.meteo.exceptions.CityNotFoundException;
import com.nkomoelate.meteo.exceptions.WeatherProviderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class GeoCodingClient {
    private final RestClient restClient;

    public GeoCodingClient(@Value("${openmeteo.geocoding.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public GeoCodingResult findCity(String city) throws CityNotFoundException {
        try {
            GeoCodingResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/search")
                            .queryParam("name", city)
                            .queryParam("count", 1)
                            .queryParam("language", "fr")
                            .build())
                    .retrieve()
                    .body(GeoCodingResponse.class);


            if (response == null)
                throw new WeatherProviderException("Reponse de l'API geocoding invalide!");
            else if (response.results() == null || response.results().isEmpty()) {
                throw new CityNotFoundException(city);
            }
            return response.results().getFirst();
        }catch(RestClientException restClientException){
            throw new WeatherProviderException("API geocoding indisponible",restClientException);
        }
    }
}
