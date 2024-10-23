package com.example.springweb.integration.service;

import com.example.springweb.exceptions.ApiErrorCode;
import com.example.springweb.exceptions.ExternalServiceException;
import com.example.springweb.integration.models.Weather;
import com.example.springweb.integration.models.WeatherRequest;
import com.example.springweb.property.WeatherProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class WeatherServiceImp implements WeatherService {
    private final WeatherProperty weatherProperty;
    private final RestClient weatherRestclient;

    public WeatherServiceImp(WeatherProperty weatherProperty) {
        this.weatherProperty = weatherProperty;
        weatherRestclient = RestClient.builder()
                .baseUrl(weatherProperty.getUrl())
                .build();
    }

    @Override
    public WeatherRequest getUVIndex(Double latitude, Double longitude) {
        String url = String.format("%s,%s?key=%s", latitude, longitude, weatherProperty.getKey());
        Weather response = weatherRestclient.get()
                .uri(url)
                .retrieve()
                .body(Weather.class);
        return Optional.ofNullable(response)
                .map(Weather::getWeatherRequest)
                .orElseThrow(() -> new ExternalServiceException("Weather data not found",
                        ApiErrorCode.WEATHER_DATA_NOT_FOUND));
    }
}