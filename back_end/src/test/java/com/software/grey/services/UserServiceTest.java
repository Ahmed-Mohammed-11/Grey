package com.software.grey.services;

import com.software.grey.models.dtos.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;
    @Test
    void userExistsShouldBeTrue() {
        UserDTO user = new UserDTO("a@gmail.com", "a", "pass");
        userService.save(user);
        assertTrue(userService.userExists(user));
    }

    @Test
    void userExistsShouldBeFalse() {
        UserDTO user = new UserDTO("iDoNotExist@gmail.com", "accursedNotExists", "pass");
        assertFalse(userService.userExists(user));
    }
}