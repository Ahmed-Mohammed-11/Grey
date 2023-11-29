package com.software.grey.controllers;

import com.software.grey.models.dtos.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.software.grey.utils.EndPoints.LOGIN_SUCCESS;
import static com.software.grey.utils.EndPoints.SIGNUP;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
public class LoginController {
    @GetMapping(LOGIN_SUCCESS)
    public ResponseEntity<String> loginSuccess() {
        return ResponseEntity.status(HttpStatus.OK).body("Log in success!");
    }
}
