package com.software.grey.controller;

import com.software.grey.SavedPostEnum;
import com.software.grey.services.SavedPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "post")
@CrossOrigin
public class Controller {

    private SavedPostService savedPostService;

    Controller(SavedPostService savedPostService) {
        this.savedPostService = savedPostService;
    }

    @PostMapping(path = "/toggle/save/{id}")
    public ResponseEntity<String> savePost(@PathVariable("id") UUID postId) {
        SavedPostEnum saved = savedPostService.saveUnsavePost(postId);
        if (saved == SavedPostEnum.SAVED) {
            return new ResponseEntity<>("Saved successfully", HttpStatus.OK);
        } else if (saved == SavedPostEnum.REMOVED) {
            return new ResponseEntity<>("Removed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }
}
