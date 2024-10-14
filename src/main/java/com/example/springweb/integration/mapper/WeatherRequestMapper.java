package com.example.springweb.integration.mapper;

import com.example.springweb.integration.controllers.WeatherRequestDto;
import com.example.springweb.integration.models.WeatherRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeatherRequestMapper {
    WeatherRequestDto toDto(WeatherRequest weatherRequest);
}
