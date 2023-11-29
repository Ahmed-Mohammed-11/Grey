package com.software.grey;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
    @GetMapping("grey")
    public String helloWorld() {
        return "Hello Greyss";
    }
}
