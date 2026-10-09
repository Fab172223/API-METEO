package com.nkomoelate.meteo.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherResponse(
        double latitude,
        double longitude,
        @JsonProperty("timezone") String timeZone,
        DailyWeather daily
) {}
