package com.example.springweb.integration.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @JsonProperty("region")
    String region;
    @JsonProperty("city")
    String city;
    @JsonProperty("town")
    String town;
    @JsonProperty("village")
    String village;
    @JsonProperty("country")
    String country;
}
