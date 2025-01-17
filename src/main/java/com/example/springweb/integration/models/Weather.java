package com.example.springweb.integration.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    @JsonProperty("currentConditions")
    private WeatherRequest weatherRequest;
}
