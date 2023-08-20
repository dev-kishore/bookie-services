package com.movie.booker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
// import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// import com.google.gson.Gson;
import com.movie.booker.dto.Message;
import com.movie.booker.dto.Movie;
import com.movie.booker.dto.MovieRequest;
import com.movie.booker.dto.Ticket;
import com.movie.booker.repository.MovieRepository;
import com.movie.booker.repository.TicketRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MovieService {

    private final MovieRepository repository;

    private final MongoTemplate mongoTemplate;

    private final TicketRepository ticketRepository;

    // private final KafkaTemplate<String, String> kafkaTemplate;

    public Message addMovie(MovieRequest movieDetails, MultipartFile file) throws IOException {
        Integer tickets = 0;
        for (List<String> theatre : movieDetails.getTheatres()) {
            tickets = tickets + Integer.parseInt(theatre.get(1));
        }
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
                .theatres(movieDetails.getTheatres())
                .ticketStatus("TO BE SOLD")
                .tickets(tickets)
                .build();
        repository.save(movie);
        log.info("Movie added with title: {}", movie.getTitle());
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

    public Message deleteMovie(String id) {
        repository.deleteById(id);
        log.info("Movie deleted with id: {}", id);
        return Message.builder().message("Movie deleted successfully!").build();
    }

    public void updateMovieStatus(String id, Integer warnLimit) {
        Optional<Movie> movieFromDB = repository.findById(id);
        movieFromDB.ifPresent(movie -> {
            if (movie.getTickets() == 0) {
                movie.setTicketStatus("SOLD OUT");
                repository.save(movie);
            } else if (movie.getTickets() < warnLimit) {
                movie.setTicketStatus("BOOK ASAP");
                repository.save(movie);
            }
        });
    }

    public Message bookTicket(Ticket ticketDetails) {
        List<List<String>> theatres = new ArrayList<>();
        Optional<Movie> movieFromDBOptional = repository.findById(ticketDetails.getMovieId());
        if (!movieFromDBOptional.isPresent()) {
            return Message.builder().message("Movie not found with movie id " + ticketDetails.getMovieId()).build();
        }
        Movie movieFromDB = movieFromDBOptional.get();
        Ticket ticket = Ticket.builder()
                .movieId(movieFromDB.getId())
                .username(ticketDetails.getUsername())
                .title(movieFromDB.getTitle())
                .theatreName(ticketDetails.getTheatreName())
                .numberOfTickets(ticketDetails.getNumberOfTickets())
                .build();
        movieFromDB.setTickets(movieFromDB.getTickets() - ticketDetails.getNumberOfTickets());
        for (List<String> theatre : movieFromDB.getTheatres()) {
            if (theatre.get(0).equalsIgnoreCase(ticketDetails.getTheatreName())) {
                String availableTickets = String
                        .valueOf(Integer.parseInt(theatre.get(1)) - ticketDetails.getNumberOfTickets());
                theatre.set(1, availableTickets);
            }
            theatres.add(theatre);
        }
        movieFromDB.setTheatres(theatres);
        repository.save(movieFromDB);
        ticketRepository.save(ticket);
        // kafkaTemplate.send("bookie-events", new Gson().toJson(ticket));
        updateMovieStatus(ticket.getId(), 10);
        return Message.builder().message("Tickets booked successfully!").build();
    }

}
