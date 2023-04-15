package com.movie.booker.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.booker.dto.UpdateUser;
import com.movie.booker.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1.0/moviebooking")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PutMapping("/user/{username}/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUser user,
                                        @NonNull @PathVariable("username") String username) {
        return ResponseEntity.ok(userService.updateUser(user, username));
    }

}
