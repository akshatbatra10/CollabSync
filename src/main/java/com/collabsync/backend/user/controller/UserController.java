package com.collabsync.backend.user.controller;

import com.collabsync.backend.config.AppConfigProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private final AppConfigProperties config;

    @Autowired
    public UserController(AppConfigProperties config) {
        this.config = config;
    }

    @PostConstruct
    public void init() {
        System.out.println( "DB URL: " + config.getUrl());
    }
}
