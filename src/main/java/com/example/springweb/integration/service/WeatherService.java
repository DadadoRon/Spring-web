package com.example.springweb.integration.service;

import com.example.springweb.integration.models.CurrentCondition;

public interface WeatherService {
    CurrentCondition getUVIndex(Double latitude, Double longitude);
}
