package com.movie.booker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.movie.booker.dto.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    
}
