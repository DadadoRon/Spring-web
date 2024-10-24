package com.example.springweb.integration.controllers;

import com.example.springweb.integration.mapper.AddressRequestMapper;
import com.example.springweb.integration.models.AddressRequest;
import com.example.springweb.integration.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.springweb.integration.controllers.AddressController.REQUEST_MAPPING;

@RestController
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final AddressRequestMapper addressRequestMapper;
    public static final String REQUEST_MAPPING = "/api/v1/address";

    @PostMapping("/locality")
    public AddressRequestDto getLocality(@RequestBody LocationDto location) {
        AddressRequest address = addressService.getAddress(location.latitude(), location.longitude());
        return addressRequestMapper.toDto(address);
    }
}
