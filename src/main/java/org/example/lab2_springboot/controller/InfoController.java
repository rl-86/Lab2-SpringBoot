package org.example.lab2_springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class InfoController {

    @GetMapping("/")
    public String info() {
        return "Hello, World!";
    }
}

