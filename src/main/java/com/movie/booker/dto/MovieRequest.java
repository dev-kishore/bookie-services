package com.movie.booker.dto;

import java.util.List;

import lombok.Data;

@Data
public class MovieRequest {
    private String title;
    private String description;
    private String duration;
    private String genre;
    private String movieRating;
    private String language;
    private String releaseDate;
    private String viewType;
    private String location;
    private List<List<String>> theatres; 
}
