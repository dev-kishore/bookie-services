package com.movie.booker.dto;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class User {
    @Id
    private UUID id;
    private String loginId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String contactNumber;
}
