package com.example.springweb;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class   UserRepository {
    public static final List<User> USERS = new ArrayList<>(List.of(
            User.builder().id(1).firstName("Dasha").lastName("Yellow").email("yellow@mail.com").password("123").role(Role.ADMIN).build(),
            User.builder().id(2).firstName("Dasha").lastName("White").email("white@mail.com").password("124").role(Role.USER).build(),
            User.builder().id(3).firstName("Dasha").lastName("Grey").email("grey@mail.com").password("125").role(Role.USER).build(),
            User.builder().id(4).firstName("Dasha").lastName("Blue").email("blue@mail.com").password("126").role(Role.USER).build(),
            User.builder().id(5).firstName("Dasha").lastName("Black").email("black@mail.com").password("127").role(Role.USER).build()
    ));
}
