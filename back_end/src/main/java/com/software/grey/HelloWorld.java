package com.software.grey;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class HelloWorld {
    @GetMapping("grey")
    public String helloWorld() {
        return "Hello Greyss";
    }
}
