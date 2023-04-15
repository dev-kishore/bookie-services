package com.movie.booker.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.movie.booker.dto.Message;
import com.movie.booker.dto.UpdateUser;
import com.movie.booker.dto.User;
import com.movie.booker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String id) {
        return userRepository.findByUsername(id).get();
    }

    public <T> T updateUser(@Valid UpdateUser userDetails, String username) {
        Optional<User> userFromDB = userRepository.findByUsername(username);
        if(!userFromDB.isPresent()) {
            return (T) Message.builder().message("Can't find user by email to update!").build();
        }
        userFromDB.ifPresent(user -> {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setContactNumber(userDetails.getContactNumber());
            userRepository.save(user);
        });
        return (T) userDetails;
    }

}
