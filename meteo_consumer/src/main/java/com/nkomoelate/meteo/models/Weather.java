package com.nkomoelate.meteo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    private String cityName;
    private Double temperatureMax;
    private Double temperatureMin;
    private Double windSpeed;
    private Double precipitation;
    private EnumSkyWeather condition;
    private LocalDate date;
}
