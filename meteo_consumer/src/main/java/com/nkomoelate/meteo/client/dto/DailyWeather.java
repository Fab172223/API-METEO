package com.nkomoelate.meteo.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record DailyWeather(
        List<LocalDate> time,
        @JsonProperty("temperature_2m_max") List<Double> temperatureMax,
        @JsonProperty("temperature_2m_min") List<Double> temperatureMin,
        @JsonProperty("precipitation_sum") List<Double> precipitation,
        @JsonProperty("wind_speed_10m_max") List<Double> windSpeed,
        @JsonProperty("weather_code") List<Integer> weatherCode
) {}
