package com.movie.booker.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.booker.dto.Message;
import com.movie.booker.dto.Movie;
import com.movie.booker.dto.Ticket;
import com.movie.booker.service.MovieService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://bookie-angular-app.s3-website-us-east-1.amazonaws.com")
@RestController
@RequestMapping("api/v1.0/moviebooking/user")
@RequiredArgsConstructor
public class UserController {

    private final MovieService movieService;

    @GetMapping("movie/get")
    public ResponseEntity<List<Movie>> getMovie(@RequestParam("query") String name) {
        return ResponseEntity.ok(movieService.searchMovie(name));
    }

    @GetMapping("movie/search")
    public ResponseEntity<Set<String>> getSearchSuggestions(@RequestParam("query") String name) {
        return ResponseEntity.ok(movieService.getSearchSuggestions(name));
    }

    @GetMapping("movies/get")
    public ResponseEntity<List<Movie>> getMovies(@RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(movieService.getMovies(page, size));
    }

    @PostMapping("ticket/book")
    public ResponseEntity<Message> bookTicket(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(movieService.bookTicket(ticket));
    }

}
