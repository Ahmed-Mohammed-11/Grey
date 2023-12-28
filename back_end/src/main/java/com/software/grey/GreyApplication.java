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
/*
    + securityUtil -> replace userRepo with userService (cycle dependency)
    + postService -> in add post method use util.getUser() instead of userRepo.findByUsername() (tests)

 */
