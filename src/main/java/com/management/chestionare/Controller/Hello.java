package com.management.chestionare.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    @RequestMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}