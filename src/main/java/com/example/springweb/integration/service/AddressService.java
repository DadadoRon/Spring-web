package com.example.springweb.integration.service;

import com.example.springweb.integration.models.AddressRequest;

public interface AddressService {
    AddressRequest getAddress(Double latitude, Double longitude);
}
