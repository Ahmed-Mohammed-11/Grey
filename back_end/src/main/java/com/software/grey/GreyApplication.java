package com.software.grey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
public class GreyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreyApplication.class, args);
    }
}
