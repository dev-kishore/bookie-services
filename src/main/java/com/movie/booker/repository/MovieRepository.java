package com.movie.booker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.movie.booker.dto.Movie;

public interface MovieRepository extends MongoRepository<Movie, String> {

}
