package com.example.springweb;

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
                .firstName(RandomStringUtils.randomAlphabetic(8,12))
                .lastName(RandomStringUtils.randomAlphabetic(8,12))
                .email(RandomStringUtils.randomAlphabetic(5,6) + "@example.com")
                .password(RandomStringUtils.randomNumeric(8) + "@@")
                .role(role)
                .build();
    }

    public static List<User> createRandomUserList() {
        int min = 3;
        int max = 8;
        int userListSize = ThreadLocalRandom.current().nextInt(min, max);
        List<User> userList = new ArrayList<>();
        userList.add(createUser(Role.ADMIN));
        for (int i = 0; i < userListSize; i++) {
            userList.add(createUser(Role.USER));
        }
        return userList;
    }
}
