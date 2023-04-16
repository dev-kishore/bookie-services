package com.movie.booker.controller;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.booker.dto.Message;
import com.movie.booker.dto.Movie;
import com.movie.booker.dto.Ticket;
import com.movie.booker.dto.UpdateUser;
import com.movie.booker.service.MovieService;
import com.movie.booker.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1.0/moviebooking/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final MovieService movieService;


    @PutMapping("/{username}/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUser user,
                                        @NonNull @PathVariable("username") String username) {
        return ResponseEntity.ok(userService.updateUser(user, username));
    }

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

    @PostMapping("movie/ticket/book")
    public ResponseEntity<Message> bookTicket(@RequestBody Ticket ticket) {
        return ResponseEntity.ok(movieService.bookTicket(ticket));
    }

}
