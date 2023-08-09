package com.example.ToDoApp.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@Component
@ComponentScan("org.msync.spring_boost")
@RestController
@RequestMapping("/spring")
public class DevWorx {

}