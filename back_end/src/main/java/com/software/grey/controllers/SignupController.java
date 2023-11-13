package com.software.grey.controllers;

import com.software.grey.models.dtos.UserDTO;
import com.software.grey.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {
    private final UserService userService;
    public SignupController(UserService userService){
        this.userService = userService;
    }
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO){
        if(userService.userExists(userDTO)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body("User created!");
    }
}
