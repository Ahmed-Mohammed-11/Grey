package com.software.grey.services;

import com.software.grey.controllers.SignupController;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.BasicUser;
import com.software.grey.models.entities.GoogleUser;
import com.software.grey.repositories.BasicUserRepo;
import com.software.grey.repositories.GoogleUserRepo;
import com.software.grey.repositories.UserRepo;
import com.software.grey.repositories.UserVerificationRepo;
import com.software.grey.utils.SecurityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        String newUsername = "new_username";

        BasicUser oldUser = basicUserRepo.findById(userId).get();

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), newUsername, oldUser.getPassword());

        boolean updated = userService.updateUser(userDTO);

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(updated).isTrue();
        assertThat(newUser.getUsername()).isEqualTo(newUsername);
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
        assertThat(newUser.getPassword()).isEqualTo(oldUser.getPassword());
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

    @Test
    void updateAllAttributes_ShouldUpdateAllAttributes() {
        BasicUser oldUser = basicUserRepo.findById(userId).get();
        String newEmail = "allattributes@gmail.com";
        String newPassword = "everyone loves Ahmed Elnaggar";
        String newUsername = "all_attributes";

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        UserDTO userDTO = new UserDTO(newEmail, newUsername, newPassword);
        Boolean updated = userService.updateUser(userDTO);

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(updated).isTrue();
        assertThat(newUser.getUsername()).isEqualTo(newUsername);
        assertThat(newUser.getEmail()).isEqualTo(newEmail);
        assertThat(bCryptPasswordEncoder.matches(newPassword, newUser.getPassword())).isTrue();
    }

    @Test
    void updateUsername_ShouldNotUpdateUsername() {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        String newUsername = "duplicateUsername";

        UserDTO userDTO = new UserDTO("userwithsameusername@gmail.com", newUsername,
                "duplicate user password");
        signupController.signup(userDTO);

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        UserDTO userDTO2 = new UserDTO(oldUser.getEmail(), newUsername, oldUser.getPassword());
        Boolean updated = userService.updateUser(userDTO2);

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(updated).isFalse();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
        assertThat(oldUser.getPassword()).isEqualTo(newUser.getPassword());
    }

    @Test
    void updateEmail_ShouldNotUpdateEmail() {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        String newEmail = "duplicateemail@gmail.com";

        UserDTO userDTO = new UserDTO(newEmail, "duplicate_email",
                "duplicate email password");
        signupController.signup(userDTO);

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        UserDTO userDTO2 = new UserDTO(newEmail, oldUser.getUsername(), oldUser.getPassword());
        Boolean updated = userService.updateUser(userDTO2);

        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(updated).isFalse();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
        assertThat(oldUser.getPassword()).isEqualTo(newUser.getPassword());
    }

    @Test
    void updatePassword_ShouldNotUpdatePassword() {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        String newPassword = "InvalidPassword";

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), oldUser.getUsername(), newPassword);

        boolean updated = userService.updateUser(userDTO);
        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(updated).isFalse();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
        assertThat(oldUser.getPassword()).isEqualTo(newUser.getPassword());
    }

    @Test
    void updateUsername_ShouldNotUpdateUsername_InvalidUsername() {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        String newUsername = "Invalid Username";

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), newUsername, oldUser.getPassword());

        boolean updated = userService.updateUser(userDTO);
        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(updated).isFalse();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
        assertThat(oldUser.getPassword()).isEqualTo(newUser.getPassword());
    }

    @Test
    void updateEmail_ShouldNotUpdateEmail_InvalidEmail() {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        String newEmail = "Invalid Email";

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        UserDTO userDTO = new UserDTO(newEmail, oldUser.getUsername(), oldUser.getPassword());

        boolean updated = userService.updateUser(userDTO);
        BasicUser newUser = basicUserRepo.findById(userId).get();
        assertThat(updated).isFalse();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
        assertThat(oldUser.getPassword()).isEqualTo(newUser.getPassword());
    }

    @Test
    void testNullInput() {
        BasicUser oldUser = basicUserRepo.findById(userId).get();

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        boolean updated = userService.updateUser(null);
        assertThat(updated).isFalse();
    }

    @Test
    void testUpdateGoogleUser_shouldUpdateUsername() {
        UserDTO oldDTO = new UserDTO("ily@gmail.com", "ily_504", null);
        userService.saveGoogleUser(oldDTO);

        GoogleUser oldUser = googleUserRepo.findByUsername(oldDTO.username);

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        String newUsername = "google_user";

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), newUsername, null);
        boolean updated = userService.updateUser(userDTO);

        GoogleUser newUser = googleUserRepo.findByUsername(newUsername);
        assertThat(updated).isTrue();
        assertThat(newUser.getUsername()).isEqualTo(newUsername);
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
    }

    @Test
    void updateGoogleEmail_shouldNotUpdate() {
        UserDTO oldDTO = new UserDTO("invalidemaltest@gmail.com", "updateemail", null);
        userService.saveGoogleUser(oldDTO);

        GoogleUser oldUser = googleUserRepo.findByUsername(oldDTO.username);

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        String newEmail = "yoyo@gmail.com";

        UserDTO userDTO = new UserDTO(newEmail, oldUser.getUsername(), null);
        boolean updated = userService.updateUser(userDTO);

        GoogleUser newUser = googleUserRepo.findByUsername(oldUser.getUsername());
        assertThat(updated).isFalse();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
    }

    @Test
    void updateGoogleUsername_shouldNotUpdate() {
        UserDTO oldDTO = new UserDTO("invalidgoogleusername@gmail", "googleuser_invalid", null);
        userService.saveGoogleUser(oldDTO);

        GoogleUser oldUser = googleUserRepo.findByUsername(oldDTO.username);

        when(securityUtils.getCurrentUser()).thenReturn(oldUser);

        String newUsername = "google username";

        UserDTO userDTO = new UserDTO(oldUser.getEmail(), newUsername, null);
        boolean updated = userService.updateUser(userDTO);

        GoogleUser newUser = googleUserRepo.findByUsername(oldUser.getUsername());
        assertThat(updated).isFalse();
        assertThat(newUser.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(newUser.getEmail()).isEqualTo(oldUser.getEmail());
    }
}
