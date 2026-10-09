package com.nkomoelate.meteo.services;

import com.nkomoelate.meteo.client.GeoCodingClient;
import com.nkomoelate.meteo.client.WeatherClient;
import com.nkomoelate.meteo.client.dto.DailyWeather;
import com.nkomoelate.meteo.client.dto.GeoCodingResult;
import com.nkomoelate.meteo.models.EnumSkyWeather;
import com.nkomoelate.meteo.models.Weather;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeteoService {

    private final WeatherClient weatherClient;
    private final GeoCodingClient geoCodingClient;
    public MeteoService(WeatherClient weatherClient, GeoCodingClient geoCodingClient){
        this.weatherClient = weatherClient;
        this.geoCodingClient = geoCodingClient;
    }
    public List<Weather> getWeatherByCity(String city) {
        GeoCodingResult geoCodingResult = this.geoCodingClient.findCity(city);
        DailyWeather daily= weatherClient.getDailyForeCast(geoCodingResult.latitude(), geoCodingResult.longitude());
        return toWeatherList(geoCodingResult.name(), daily);
    }
    private List<Weather> toWeatherList(String cityName, DailyWeather daily){
        List<Weather> weathers= new ArrayList<>();
        for(int i=0;i<daily.time().size();i++){
            Weather weather = new Weather();
            weather.setCityName(cityName);
            weather.setDate(daily.time().get(i));
            weather.setTemperatureMin(daily.temperatureMin().get(i));
            weather.setPrecipitation(daily.precipitation().get(i));
            weather.setTemperatureMax(daily.temperatureMax().get(i));
            weather.setWindSpeed(daily.windSpeed().get(i));
            weather.setCondition(EnumSkyWeather.getLabelFromCode(daily.weatherCode().get(i)));
            weathers.add(weather);
        }
        return weathers;
    }
}
