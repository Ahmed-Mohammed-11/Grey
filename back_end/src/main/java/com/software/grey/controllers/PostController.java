package com.software.grey.controllers;

import com.software.grey.SavedPostEnum;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.services.SavedPostService;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.EndPoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(EndPoints.POST)
@AllArgsConstructor
public class PostController {

    private PostService postService;
    private SavedPostService savedPostService;

    @Operation(
            summary = "Create a new post",
            description = "Use this end point to enable the user to create new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Post was not save due to invalid request body"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @PostMapping(EndPoints.ADD_POST)
    public ResponseEntity<UUID> addPost(@Valid @RequestBody PostDTO postDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.add(postDTO));
    }

    @PostMapping(path = EndPoints.SAVE_POST + "/{id}")
    public ResponseEntity<String> savePost(@PathVariable("id") UUID postId) {
        SavedPostEnum saved = savedPostService.toggleSavedPost(postId);
        if (saved == SavedPostEnum.SAVED) {
            return new ResponseEntity<>("Saved successfully", HttpStatus.OK);
        } else if (saved == SavedPostEnum.REMOVED) {
            return new ResponseEntity<>("Removed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }
}
