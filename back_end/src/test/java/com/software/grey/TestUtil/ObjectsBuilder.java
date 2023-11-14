package com.software.grey.TestUtil;

import com.software.grey.models.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ObjectsBuilder {

    public User createUserA() {
        User userA = User.builder()
                .email("abdulrahman@gmail.com")
                .password("sha256jj")
                .username("abdulrahman")
                .build();
        return userA;
    }

    public User createUserB() {
        User userA = User.builder()
                .email("h@gmail.com")
                .password("xxxxxx")
                .username("7a7a")
                .build();
        return userA;
    }

    public User createUserC() {
        User userA = User.builder()
                .email("hamada@gmail.com")
                .password("Manga")
                .username("Eren eager")
                .build();
        return userA;
    }
}
