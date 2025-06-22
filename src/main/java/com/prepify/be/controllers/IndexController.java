package com.prepify.be.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(IndexController.MODULE_PATH)
public class IndexController {
    public static final String MODULE_PATH = "/api/check";

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
