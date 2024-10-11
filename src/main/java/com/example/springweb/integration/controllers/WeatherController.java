package com.example.springweb.integration.controllers;

import com.example.springweb.integration.mapper.CurrentConditionMapper;
import com.example.springweb.integration.models.CurrentCondition;
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
    private final CurrentConditionMapper currentConditionMapper;
    public static final String REQUEST_MAPPING = "/api/v1";


    @PostMapping("/uvindex")
    public CurrentConditionDto getUVIndex(@RequestBody LocationDto location) {
        CurrentCondition currentCondition = weatherService.getUVIndex(location.latitude(), location.longitude());
        return currentConditionMapper.toDto(currentCondition);
    }
}
