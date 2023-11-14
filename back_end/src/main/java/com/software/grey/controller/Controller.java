package com.software.grey.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/grey")
public class Controller {
    @GetMapping(path = "/savePost")
    public @ResponseBody String savePost() {
        return "Successfully saved";
    }
}
