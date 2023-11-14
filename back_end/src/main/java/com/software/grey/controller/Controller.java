package com.software.grey.controller;

import com.software.grey.models.dtos.SavedPostDto;
import com.software.grey.services.SavedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/grey")
@CrossOrigin
public class Controller {

    SavedPostService savedPostService;

    @Autowired
    Controller(SavedPostService savedPostService) {
        this.savedPostService = savedPostService;
    }

    @GetMapping(path = "/saveUnsavePost")
    public ResponseEntity<String> savePost(@RequestBody SavedPostDto savedPostDto) {
        Integer saved = savedPostService.saveUnsavePost(savedPostDto);
        if (saved == 1) {
            return new ResponseEntity<>("Saved successfully", HttpStatus.OK);
        } else if (saved == 0) {
            return new ResponseEntity<>("Removed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }
}
