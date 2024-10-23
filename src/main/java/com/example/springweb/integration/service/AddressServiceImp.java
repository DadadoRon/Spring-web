package com.example.springweb.integration.service;

import com.example.springweb.exceptions.ApiErrorCode;
import com.example.springweb.exceptions.ExternalServiceException;
import com.example.springweb.integration.models.AddressRequest;
import com.example.springweb.integration.models.FullAddress;
import com.example.springweb.property.AddressProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AddressServiceImp implements AddressService {
    private final RestClient adressRestclient;

    public AddressServiceImp(AddressProperty addressProperty) {
        adressRestclient = RestClient.builder()
                .baseUrl(addressProperty.getUrl())
                .build();
    }

    @Override
    public AddressRequest getAddress(Double latitude, Double longitude) {
        String url = String.format("?lat=%s&lon=%s", latitude, longitude);
        System.out.println(url);
        FullAddress response = adressRestclient.get()
                .uri(url)
                .retrieve()
                .body(FullAddress.class);
        return Optional.ofNullable(response)
                .map(FullAddress::getAddress)
                .map(it -> {
                    String locality = Stream.of(it.getCity(), it.getTown(), it.getRegion(), it.getVillage())
                            .filter(Objects::nonNull)
                            .findFirst()
                            .orElse(it.getCountry());
                    return new AddressRequest(locality);
                        })
                .orElseThrow(() -> new ExternalServiceException("Address not found",
                        ApiErrorCode.ADDRESS_DATA_NOT_FOUND));
        }
}
