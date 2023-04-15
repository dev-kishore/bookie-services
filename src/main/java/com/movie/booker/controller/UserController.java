package com.movie.booker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.booker.dto.User;
import com.movie.booker.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1.0/moviebooking")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/user/{id}")
    private ResponseEntity<User> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

}
