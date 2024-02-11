package com.software.grey.controllers;

import com.software.grey.models.dtos.responseDTOs.FeelingDashboardDTO;
import com.software.grey.services.implementations.FeelingDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.software.grey.utils.EndPoints.FEELING_DASHBOARD;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping(FEELING_DASHBOARD)
public class FeelingDashboardController {
    private FeelingDashboardService feelingDashboardService;

    @Operation(
            summary = "Get the content of feeling dashboard",
            description = "The content of the feeling dashboard is the total number of posts, the total number of user posts, the user feelings and the global feelings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Content of the feeling dashboard retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Endpoint not found"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @GetMapping()
    public ResponseEntity<FeelingDashboardDTO> getDashboardContent() {
        return ResponseEntity.status(HttpStatus.OK).body(feelingDashboardService.getFeelingDashboardContent());
    }
}
