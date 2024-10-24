package com.example.springweb.property;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("address")
@Data
@Validated
@Component
public class AddressProperty {
    @NotBlank
    private String url;
}
