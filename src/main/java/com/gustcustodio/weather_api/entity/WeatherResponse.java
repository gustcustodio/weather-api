package com.gustcustodio.weather_api.entity;

import java.io.Serializable;

public record WeatherResponse(String city, double temp, String description) implements Serializable {
}
