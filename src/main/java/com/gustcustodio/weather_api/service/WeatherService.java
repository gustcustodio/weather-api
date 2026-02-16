package com.gustcustodio.weather_api.service;

import com.gustcustodio.weather_api.config.RestConfig;
import com.gustcustodio.weather_api.entity.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;


@Service
public class WeatherService {

    @Value("${api.weather.key}")
    private String apiKey;

    private RestConfig restConfig;
    private RedisTemplate<String, Object> redisTemplate;

    public WeatherService(RestConfig restConfig, RedisTemplate<String, Object> redisTemplate) {
        this.restConfig = restConfig;
        this.redisTemplate = redisTemplate;
    }

    public WeatherResponse getWeather(String city) {
        String cacheKey = "weather: " + city.toLowerCase();
        WeatherResponse cachedWeather = (WeatherResponse) redisTemplate.opsForValue().get(cacheKey);

        if (cachedWeather != null) {
            System.out.println("Cache HIT for city: " + city);
            return cachedWeather;
        }

        System.out.println("Cache MISS for city: " + city + ". Fetching from external API...");
        String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + city + "/?key=" + apiKey;

        var response = restConfig.restTemplate().getForObject(url, Map.class);
        var currentConditions = (Map<String, Object>) response.get("currentConditions");

        WeatherResponse weather = new WeatherResponse(
                city,
                (double) currentConditions.get("temp"),
                (String) currentConditions.get("conditions")
        );

        redisTemplate.opsForValue().set(cacheKey, weather, Duration.ofHours(12));

        return weather;
    }

}
