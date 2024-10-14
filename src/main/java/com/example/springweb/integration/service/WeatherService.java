package com.example.springweb.integration.service;

import com.example.springweb.integration.models.WeatherRequest;

public interface WeatherService {
    WeatherRequest getUVIndex(Double latitude, Double longitude);
}
