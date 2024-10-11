package com.example.springweb.integration.service;

import com.example.springweb.integration.models.CurrentCondition;
import com.example.springweb.integration.models.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;


@Service
public class WeatherServiceImp implements WeatherService {
    private final RestClient restclient;

    public  WeatherServiceImp() {
      restclient = RestClient.builder()
              .baseUrl(BASE_URL)
              .build();
    }
    @Value("${weather.api.key}")
    private String API_KEY;

    @Value("${weather.api.url}")
    private String BASE_URL;


    public CurrentCondition getUVIndex(Double latitude, Double longitude) {
        String url = String.format("%s%s,%s?key=%s", BASE_URL, latitude, longitude, API_KEY);
        Weather response = restclient.get()
                .uri(url)
                .retrieve()
                .body(Weather.class);
        return Optional.ofNullable(response)
                .map(Weather::getCurrentConditions)
                .orElseThrow(() -> new RuntimeException("Weather data not found"));
    }
}