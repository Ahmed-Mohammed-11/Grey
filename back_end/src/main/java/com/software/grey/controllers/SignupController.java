package com.software.grey.controllers;

import com.software.grey.models.dtos.UserDTO;
import com.software.grey.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.software.grey.utils.EndPoints.*;

@CrossOrigin
@RestController
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(SIGNUP)
    public ResponseEntity<String> signup(@RequestBody @Valid UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body("User created!");
    }

    @PutMapping(VERIFY_REGISTRATION)
    public ResponseEntity<String> verify(@RequestParam String userID, @RequestParam String registrationCode) {
        userService.verifyUser(userID, registrationCode);
        return ResponseEntity.status(HttpStatus.OK).body("User verified");
    }

}
