package com.example.springweb;

import com.example.springweb.controllers.user.UserCreateDto;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class UserModels {

    public static User createUser(Role role) {
        return User.builder()
                .id(null)
                .firstName(RandomStringUtils.randomAlphabetic(8, 12))
                .lastName(RandomStringUtils.randomAlphabetic(8, 12))
                .email(RandomStringUtils.randomAlphabetic(5, 6) + "@example.com")
                .password(RandomStringUtils.randomAlphabetic(8) + "@@")
                .role(role)
                .salt(RandomStringUtils.randomAlphabetic(16))
                .build();
    }

    public static List<User> getRandomUserList() {
        int min = 3;
        int max = 8;
        int userListSize = ThreadLocalRandom.current().nextInt(min, max);
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < userListSize; i++) {
            userList.add(createUser(Role.USER));
        }
        return userList;
    }


    public static UserCreateDto getUserCreateDto(Role role) {
        return new UserCreateDto(
                RandomStringUtils.randomAlphabetic(8, 12),
                RandomStringUtils.randomAlphabetic(8, 12),
                RandomStringUtils.randomAlphabetic(5, 6) + "@example.com",
                RandomStringUtils.randomNumeric(8) + "@@",
                role
                );
    }


    public static List<UserCreateDto> getRandomUserCreateDtoList() {
        int min = 3;
        int max = 8;
        int userCreateDtoListSize = ThreadLocalRandom.current().nextInt(min, max);
        List<UserCreateDto> userCreateDtoList = new ArrayList<>();
        for (int i = 0; i < userCreateDtoListSize; i++) {
            userCreateDtoList.add(getUserCreateDto(Role.USER));
        }
        return userCreateDtoList;
    }
}
