package com.collabsync.backend.user.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Value( "${DB_URL}")
    private String url;

    @PostConstruct
    public void init() {
        System.out.println( "DB URL: " + url);
    }
}
