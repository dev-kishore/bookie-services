package com.movie.booker.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.movie.booker.dto.Message;
import com.movie.booker.dto.MovieRequest;
import com.movie.booker.service.MovieService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1.0/moviebooking/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final MovieService movieService;
    @PostMapping("/movie/add")
    public ResponseEntity<Message> addMovie(@RequestParam("photo") MultipartFile file, @RequestParam("data") String movie) throws IOException {
        MovieRequest movieRequest = new Gson().fromJson(movie, MovieRequest.class);
        return ResponseEntity.ok(movieService.addMovie(movieRequest, file));
    }

    @DeleteMapping("/movie/delete/{id}")
    public ResponseEntity<Message> deleteMovie(@PathVariable("id") String id) {
        return ResponseEntity.ok(movieService.deleteMovie(id));
    }

}
