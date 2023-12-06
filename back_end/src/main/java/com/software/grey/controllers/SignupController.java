package com.software.grey.controllers;

import com.software.grey.exceptions.UserExistsException;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.software.grey.utils.EndPoints.SIGNUP;

@CrossOrigin
@RestController
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(SIGNUP)
    public ResponseEntity<String> signup(@RequestBody @Valid UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body("User created!");
    }
}
