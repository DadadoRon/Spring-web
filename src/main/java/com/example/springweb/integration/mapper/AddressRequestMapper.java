package com.example.springweb.integration.mapper;

import com.example.springweb.integration.controllers.AddressRequestDto;
import com.example.springweb.integration.models.AddressRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressRequestMapper {
    AddressRequestDto toDto(AddressRequest address);
}
