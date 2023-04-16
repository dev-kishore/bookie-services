package com.movie.booker.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.movie.booker.dto.Message;
import com.movie.booker.dto.Movie;
import com.movie.booker.service.MovieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1.0/moviebooking/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final MovieService movieService;
    @PostMapping("/movie/add")
    public ResponseEntity<Message> addMovie(@RequestPart("photo") MultipartFile file, @RequestPart("data") Movie movie) throws IOException {
        return ResponseEntity.ok(movieService.addMovie(movie, file));
    }

}
