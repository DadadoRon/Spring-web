package com.example.springweb;

import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminCreateDto;
import com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserCreateDto;
import com.example.springweb.entity.Product;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserAppointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class UserAppointmentModels {

    public static UserAppointment createUserAppointment(User user, Product product) {
        int minHours = 9;
        int maxHours = 17;
        int hour = ThreadLocalRandom.current().nextInt(minHours, maxHours);
        return UserAppointment.builder()
                .id(null)
                .date(LocalDate.now().plusDays(10))
                .time(LocalTime.of(hour, 0))
                .user(user)
                .product(product)
                .build();
    }

    public static List<UserAppointment> createUserAppointmentList(User user, Product product) {
        int min = 3;
        int max = 8;
        int userAppointmentListSize = ThreadLocalRandom.current().nextInt(min, max);
        List<UserAppointment> userAppointmentList = new ArrayList<>();
        for (int i = 0; i < userAppointmentListSize; i++) {
            userAppointmentList.add(createUserAppointment(user, product));
        }
        return userAppointmentList;
    }

    public static UserAppointmentByAdminCreateDto createUserAppointmentByAdminDto (Integer userId, Integer productId) {
        int minHours = 9;
        int maxHours = 17;
        int hour = ThreadLocalRandom.current().nextInt(minHours, maxHours);
        return UserAppointmentByAdminCreateDto.builder()
                .date(LocalDate.now().plusDays(10))
                .time(LocalTime.of(hour, 0))
                .userId(userId)
                .productId(productId)
                .build();
    }

    public static UserAppointmentByUserCreateDto createUserAppointmentByUserDto (Integer productId) {
        int minHours = 9;
        int maxHours = 17;
        int hour = ThreadLocalRandom.current().nextInt(minHours, maxHours);
        return UserAppointmentByUserCreateDto.builder()
                .date(LocalDate.now().plusDays(10))
                .time(LocalTime.of(hour, 0))
                .productId(productId)
                .build();
    }
}

