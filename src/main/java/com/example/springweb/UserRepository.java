package com.example.springweb;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class   UserRepository {
    public static final List<User> USERS = new ArrayList<>(List.of(
            new User(1, "Dasha", "Dubik"),
            new User(2, "Mike", "Green"),
            new User(3, "Nike", "Black")));

}
