package com.example.springweb.mapper;

import com.example.springweb.controllers.user.UserCreateDto;
import com.example.springweb.controllers.user.UserDto;
import com.example.springweb.controllers.user.UserSearchDto;
import com.example.springweb.controllers.user.UserUpdateDto;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserSearch;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

//    @Mapping(target = "id", ignore = true)
    User toUserForCreate(UserCreateDto createDto);

    User toUserForUpdate(UserUpdateDto updateDto);

    UserSearch toUserForSearch(UserSearchDto searchDto);
}
