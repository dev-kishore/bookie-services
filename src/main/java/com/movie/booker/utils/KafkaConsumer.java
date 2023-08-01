package com.movie.booker.utils;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.movie.booker.dto.Ticket;
import com.movie.booker.service.MovieService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MovieService movieService;

    @KafkaListener(topics = "bookie-events", groupId = "bookie")
    private void kafkaConsumer(String message){
        movieService.updateMovieStatus(new Gson().fromJson(message, Ticket.class).getMovieId(), 10);
    }
    
}
