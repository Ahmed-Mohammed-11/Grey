package com.software.grey.controllers;

import com.software.grey.SavedPostEnum;
import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.services.SavedPostService;
import com.software.grey.services.implementations.PostService;
import com.software.grey.utils.EndPoints;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(EndPoints.POST)
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<UUID> addPost(@Valid @RequestBody PostDTO postDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.add(postDTO));
    }

    @PostMapping(path = EndPoints.SAVE_POST + "/{id}")
    public ResponseEntity<String> savePost(@PathVariable("id") String postId) {
        SavedPostEnum saved = savedPostService.toggleSavedPost(postId);
        if (saved == SavedPostEnum.SAVED) {
            return new ResponseEntity<>("Saved successfully", HttpStatus.OK);
        } else if (saved == SavedPostEnum.REMOVED) {
            return new ResponseEntity<>("Removed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Get the Diary",
            description = "Get all the posts of a user with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved correctly")
    })
    @PostMapping(EndPoints.GET_DIARY)
    public ResponseEntity<Page<PostDTO>> getDiary(@Valid @RequestBody PostFilterDTO postFilterDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getDiary(postFilterDTO));
    }

    @Operation(
            summary = "Get Feed",
            description = "Get all the posts of that the other users wrote with pagination and filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved correctly")
    })
    @PostMapping(EndPoints.GET_FEED)
    public ResponseEntity<Page<PostDTO>> getFeed(@Valid @RequestBody PostFilterDTO postFilterDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getFeed(postFilterDTO));
    }

    @Operation(
            summary = "Report a post",
            description = "Use this end point to enable the user to report a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post reported successfully"),
            @ApiResponse(responseCode = "400", description = "Post already reported"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping(EndPoints.REPORT_POST + "/{id}")
    public ResponseEntity<String> reportPost(@PathVariable("id") String postId) {
        postService.report(postId);
        return ResponseEntity.status(HttpStatus.OK).body("Post reported successfully!\n" +
                "We will review your report and take the necessary actions.");
    }


    @Operation(
            summary = "Delete a post",
            description = "This endpoint is used to delete a post from user's created posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Not Authorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @DeleteMapping(EndPoints.DELETE_POST + "/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") String postId) {
        postService.delete(postId);
        return ResponseEntity.status(HttpStatus.OK).body("Post was deleted successfully!");
    }

    @DeleteMapping(EndPoints.DELETE_REPORTED_POST + "/{id}")
    public ResponseEntity<String> deleteReportedPost(@PathVariable("id") String postId) {
        postService.deleteReportedPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body("Post was deleted successfully!");
    }

    @DeleteMapping(EndPoints.REMOVE_REPORTED_POST + "/{id}")
    public ResponseEntity<String> removeReportedPost(@PathVariable("id") String postId) {
        postService.removeReportedPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body("Post is safe!");
    }
}
