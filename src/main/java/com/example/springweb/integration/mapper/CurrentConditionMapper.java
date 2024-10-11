package com.example.springweb.integration.mapper;

import com.example.springweb.integration.controllers.CurrentConditionDto;
import com.example.springweb.integration.models.CurrentCondition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrentConditionMapper {
    CurrentConditionDto toDto(CurrentCondition currentCondition);
}
