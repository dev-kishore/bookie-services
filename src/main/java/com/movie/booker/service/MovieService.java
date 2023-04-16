package com.movie.booker.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.movie.booker.dto.Message;
import com.movie.booker.dto.Movie;
import com.movie.booker.repository.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository repository;

    private final MongoTemplate mongoTemplate;

    public Message addMovie(Movie movieDetails, MultipartFile file) throws IOException {
        Movie movie = Movie.builder()
                            .title(movieDetails.getTitle())
                            .description(movieDetails.getDescription())
                            .photo(new Binary(BsonBinarySubType.BINARY, file.getBytes()))
                            .duration(movieDetails.getDuration())
                            .genre(movieDetails.getGenre())
                            .movieRating(movieDetails.getMovieRating())
                            .language(movieDetails.getLanguage())
                            .releaseDate(movieDetails.getReleaseDate())
                            .viewType(movieDetails.getViewType())
                            .location(movieDetails.getLocation())
                            .theatreName(movieDetails.getTheatreName())
                            .build();
        repository.save(movie);
        return Message.builder().message("Movie added successfully!").build();
    }

    public List<Movie> searchMovie(String name) {
        Query query = new Query(Criteria.where("title").regex(name, "i"));
        return mongoTemplate.find(query, Movie.class);
    }

    public Set<String> getSearchSuggestions(String searchTerm) {
        Query query = new Query(Criteria.where("title").regex(searchTerm, "i")).limit(10);
        return mongoTemplate.find(query, Movie.class).stream().map(Movie::getTitle).collect(Collectors.toSet());
    }

    public List<Movie> getMovies(int page, int size) {
        final Pageable paging = PageRequest.of(page, size);
        return mongoTemplate.find(new Query().with(paging), Movie.class);
    }
    
}
