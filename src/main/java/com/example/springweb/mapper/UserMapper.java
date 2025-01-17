package com.example.springweb.mapper;

import com.example.springweb.controllers.user.*;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserSearch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "salt", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toUserForCreate(UserCreateDto createDto);

    @Mapping(target = "salt", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toUserForRegister(UserRegisterDto registerDto);

    @Mapping(target = "salt", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUserForUpdate(UserUpdateDto updateDto);

    UserSearch toUserForSearch(UserSearchDto searchDto);
}
