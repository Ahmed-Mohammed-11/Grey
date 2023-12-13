package com.software.grey.controllers;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.dtos.PostFilterDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.services.implementations.ExploreService;
import com.software.grey.utils.EndPoints;
import com.sun.mail.iap.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping(EndPoints.POST)
@CrossOrigin(origins = "http://localhost:3000")
public class ExploreController {

    ExploreService exploreService;

    @GetMapping(EndPoints.EXPLORE)
    public ResponseEntity<List<PostDTO>> getExplore(@RequestParam int pageNumber, @RequestParam int pageSize){
        return ResponseEntity.status(HttpStatus.OK)
                        .body(exploreService.getRecommendedPosts(pageNumber, pageSize));
    }
}
