package com.movie.booker.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.movie.booker.dto.Message;
import com.movie.booker.dto.Movie;
import com.movie.booker.service.MovieService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/v1.0/moviebooking/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final MovieService movieService;
    @PostMapping("/movie/add")
    public ResponseEntity<Message> addMovie(@RequestPart("photo") MultipartFile file, @RequestPart("data") Movie movie) throws IOException {
        return ResponseEntity.ok(movieService.addMovie(movie, file));
    }

    @DeleteMapping("/movie/delete/{id}")
    public ResponseEntity<Message> deleteMovie(@PathVariable("id") String id) {
        return ResponseEntity.ok(movieService.deleteMovie(id));
    }

    @PutMapping("movie/{id}/status/update")
    public ResponseEntity<Message> updateMovieStatus(@PathVariable("id") String id, @RequestParam("limit") String limit) {
        return ResponseEntity.ok(movieService.updateMovieStatus(id, limit == null ? 10 : Integer.parseInt(limit)));
    }

}
