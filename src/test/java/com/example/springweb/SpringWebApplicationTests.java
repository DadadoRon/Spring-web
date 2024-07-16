package com.example.springweb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(PostgreSQLContainerExtension.class)
class SpringWebApplicationTests {

    @Test
    void contextLoads() {
    }
}
