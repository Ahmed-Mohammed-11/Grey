package com.software.grey.services;

import com.software.grey.models.dtos.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {
    @Autowired
    UserService userService;
    @Test
    void userExistsShouldBeTrue() {
        UserDTO user = new UserDTO("aad12356hb@gmail.com", "aad24", "pass");
        userService.save(user);
        assertTrue(userService.userExists(user));
    }

    @Test
    void userExistsShouldBeFalse() {
        UserDTO user = new UserDTO("iDoNotExist@gmail.com", "accursedNotExists", "pass");
        assertFalse(userService.userExists(user));
    }
}