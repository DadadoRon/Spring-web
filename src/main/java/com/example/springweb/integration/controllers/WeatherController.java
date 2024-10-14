package com.example.springweb.integration.controllers;

import com.example.springweb.integration.mapper.WeatherRequestMapper;
import com.example.springweb.integration.models.WeatherRequest;
import com.example.springweb.integration.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.springweb.integration.controllers.WeatherController.REQUEST_MAPPING;


@RestController
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;
    private final WeatherRequestMapper weatherRequestMapper;
    public static final String REQUEST_MAPPING = "/api/v1/weather";

    @PostMapping("/uvindex")
    public WeatherRequestDto getUVIndex(@RequestBody LocationDto location) {
        WeatherRequest weatherRequest = weatherService.getUVIndex(location.latitude(), location.longitude());
        return weatherRequestMapper.toDto(weatherRequest);
    }
}
