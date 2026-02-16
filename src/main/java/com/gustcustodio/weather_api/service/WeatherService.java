package com.gustcustodio.weather_api.service;

import com.gustcustodio.weather_api.config.RestConfig;
import com.gustcustodio.weather_api.entity.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class WeatherService {

    @Value("${api.weather.key}")
    private String apiKey;

    private RestConfig restConfig;

    public WeatherService(RestConfig restConfig) {
        this.restConfig = restConfig;
    }

    public WeatherResponse getWeather(String city) {
        String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + city + "/?key=" + apiKey;

        var response = restConfig.restTemplate().getForObject(url, Map.class);
        var currentConditions = (Map<String, Object>) response.get("currentConditions");

        double temp = (double) currentConditions.get("temp");
        String desc = (String) currentConditions.get("conditions");

        return new WeatherResponse(city, temp, desc);
    }

}
