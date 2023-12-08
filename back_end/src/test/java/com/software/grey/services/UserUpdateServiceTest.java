package com.software.grey.services;

import com.software.grey.controllers.SignupController;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.BasicUser;
import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.BasicUserRepo;
import com.software.grey.repositories.GoogleUserRepo;
import com.software.grey.repositories.UserRepo;
import com.software.grey.repositories.UserVerificationRepo;
import com.software.grey.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserUpdateServiceTest {

    @MockBean
    private SecurityUtils securityUtils;
    private final UserService userService;
    private final SignupController signupController;
    private final UserRepo userRepo;
    private final BasicUserRepo basicUserRepo;
    private final GoogleUserRepo googleUserRepo;
    private final UserVerificationRepo userVerificationRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private String userId;

    @Autowired
    public UserUpdateServiceTest(UserService userService, SignupController signupController,
                                 UserRepo userRepo, BasicUserRepo basicUserRepo, GoogleUserRepo googleUserRepo,
                                 UserVerificationRepo userVerificationRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.signupController = signupController;
        this.userRepo = userRepo;
        this.basicUserRepo = basicUserRepo;
        this.googleUserRepo = googleUserRepo;
        this.userVerificationRepo = userVerificationRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @BeforeAll
    void init() {
        UserDTO myUser = new UserDTO("mockEmailSave@gmail.com", "old username",
                "mock Password test");
        signupController.signup(myUser);
        userId = userRepo.findByUsername(myUser.username).getId();
    }

    @AfterAll
    void cleanUp() {
        userVerificationRepo.deleteAll();
        basicUserRepo.deleteAll();
        googleUserRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void updateUsername_ShouldUpdateUsername() {
        String newUsername = "new username";

        BasicUser oldUser = basicUserRepo.findById(userId).get();

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), newUsername, oldUser.getPassword());

        boolean updated = userService.updateUser(userDTO);

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(updated).isTrue();
        assertThat(newUser.getUsername()).isEqualTo(newUsername);
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
        assertThat(bCryptPasswordEncoder.matches("mock Password test", newUser.getPassword())).isTrue();
        System.out.println("password:" + newUser.getPassword());
    }

    @Test
    void updatePassword_ShouldUpdatePassword() {
        BasicUser oldUser = basicUserRepo.findById(userId).get();
        String newPassword = "111 222 333";

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), oldUser.getUsername(), newPassword);
        Boolean updated = userService.updateUser(userDTO);

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(updated).isTrue();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
        assertThat(bCryptPasswordEncoder.matches(newPassword, newUser.getPassword())).isTrue();
        System.out.println("password:" + newUser.getPassword());

    }

    @Test
    void updateEmail_ShouldUpdateEmail() {
        BasicUser oldUser = basicUserRepo.findById(userId).get();
        String newEmail = "abcd@gmail.com";

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        UserDTO userDTO = new UserDTO(newEmail, oldUser.getUsername(), oldUser.getPassword());
        Boolean updated = userService.updateUser(userDTO);

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(updated).isTrue();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(newEmail);
        assertThat(newUser.getPassword()).isEqualTo(oldUser.getPassword());
    }
}
