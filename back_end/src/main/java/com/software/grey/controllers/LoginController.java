package com.software.grey.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.software.grey.utils.EndPoints.LOGIN_FAIL;
import static com.software.grey.utils.EndPoints.LOGIN_SUCCESS;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class LoginController {

    @GetMapping(LOGIN_SUCCESS)
    public ResponseEntity<String> loginSuccess() {
        return ResponseEntity.status(HttpStatus.OK).body("Log in success!");
    }

    @GetMapping(LOGIN_FAIL)
    public ResponseEntity<String> loginFail() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong credentials");
    }
}
