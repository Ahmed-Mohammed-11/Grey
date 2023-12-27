package com.software.grey.security;

import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.GoogleUser;
import com.software.grey.models.enums.Role;
import com.software.grey.repositories.GoogleUserRepo;
import com.software.grey.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Objects;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
class OAuth2LoginSuccessHandlerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private GoogleUserRepo googleUserRepo;

    @Test
    void GoogleCorrectUser() {
        UserDTO myUser = new UserDTO("mockEmail@gmail.com", "mockUser", null);
        userService.saveGoogleUser(myUser);
        GoogleUser user = googleUserRepo.findByUsername("mockUser");

        assert(Objects.equals(user.getUsername(), "mockUser"));
        assert(Objects.equals(user.getEmail(), "mockEmail@gmail.com"));
        assert(user.getRole() == Role.USER);
    }
}