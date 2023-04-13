package com.movie.booker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.movie.booker.dto.Message;
import com.movie.booker.dto.User;
import com.movie.booker.repository.UserRepository;
import com.movie.booker.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1.0/moviebooking")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    private ResponseEntity<?> register(@RequestBody User user) {
        if(userRepository.findByLoginId(user.getLoginId()).isPresent()) {
            return ResponseEntity.badRequest().body(new Gson().toJson(new Message("Username already found! try with another one.")));
        } else {
            return new ResponseEntity<User>(userService.register(user), HttpStatus.CREATED);
        }
    }

    @GetMapping("/user/{id}")
    private User getUser(@PathVariable String id) {
        System.out.println("ID "+ id);
        return userRepository.findByLoginId(id).get();
    }

}
