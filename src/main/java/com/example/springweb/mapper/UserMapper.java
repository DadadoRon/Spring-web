package com.example.springweb.mapper;

import com.example.springweb.controllers.user.*;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserSearch;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

//    @Mapping(target = "id", ignore = true)
    User toUserForCreate(UserCreateDto createDto);

    User toUserForRegister(UserRegisterDto registerDto);

    User toUserForUpdate(UserUpdateDto updateDto);

    UserSearch toUserForSearch(UserSearchDto searchDto);
}
