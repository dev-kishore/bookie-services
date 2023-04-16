package com.movie.booker.dto;

import java.util.List;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movies")
public class Movie {

    @Id
    private String id;
    private String title;
    private String description;
    private Binary photo;
    private String duration;
    private String genre;
    private String movieRating;
    private String language;
    private String releaseDate;
    private String viewType;
    private String location;
    private List<List<String>> theatres; 
    private String ticketStatus;
    private Integer tickets;

}
