package com.software.grey.controllers;

import com.software.grey.models.dtos.RecommendedPostsDTO;
import com.software.grey.services.implementations.ExploreService;
import com.software.grey.utils.EndPoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(EndPoints.POST)
@CrossOrigin(origins = "http://localhost:3000")
public class ExploreController {

    ExploreService exploreService;

    @Operation(
            summary = "Query recommendation system for posts",
            description = "Gets posts based on the recommendation system's heuristics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved correctly")
    })
    @GetMapping(EndPoints.EXPLORE)
    public ResponseEntity<RecommendedPostsDTO> getExplore(@RequestParam int pageNumber, @RequestParam int pageSize) {
        RecommendedPostsDTO recommendedPostsDTO = exploreService.getRecommendedPosts(pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK)
                .body(recommendedPostsDTO);
    }
}
