package com.software.grey.controllers;


import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.dtos.UserResponseDTO;
import com.software.grey.services.UserService;
import com.software.grey.utils.EndPoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(EndPoints.USER)
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Update the user",
            description = "Update the user with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "something went wrong")
    })
    @PutMapping(EndPoints.UPDATE_USER)
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        return ResponseEntity.ok().body("Updated successfully");
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> getUser() {
        return ResponseEntity.ok().body(userService.getUser());
    }
}
