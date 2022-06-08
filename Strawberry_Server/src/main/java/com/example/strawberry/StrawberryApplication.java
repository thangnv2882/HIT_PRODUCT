package com.example.strawberry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.Random;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class StrawberryApplication {

    public static void main(String[] args) {
        SpringApplication.run(StrawberryApplication.class, args);
    }

}
