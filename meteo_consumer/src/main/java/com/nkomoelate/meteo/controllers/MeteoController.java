package com.nkomoelate.meteo.controllers;

import com.nkomoelate.meteo.models.Weather;
import com.nkomoelate.meteo.services.MeteoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/meteo")
public class MeteoController{

   private final MeteoService meteoService;

    public MeteoController(MeteoService meteoService){
        this.meteoService = meteoService;
    }

    @GetMapping
    public List<Weather> getWeatherByCity(@RequestParam String city){
        return meteoService.getWeatherByCity(city);
    }

}