package com.movie.booker.service;

import org.springframework.stereotype.Service;

import com.movie.booker.dto.User;
import com.movie.booker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String id) {
        return userRepository.findByUsername(id).get();
    }

}
