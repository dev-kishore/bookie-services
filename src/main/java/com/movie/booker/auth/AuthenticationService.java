package com.movie.booker.auth;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.movie.booker.config.JwtService;
import com.movie.booker.dto.Message;
import com.movie.booker.dto.Password;
import com.movie.booker.dto.Role;
import com.movie.booker.dto.User;
import com.movie.booker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    
    public <T> T register(RegisterRequest request) {
        Optional<User> userFromDB;
        userFromDB = repository.findByUsername(request.getUsername());
        if(userFromDB.isPresent()) {
            return (T) Message.builder().message("Username already found! Try with another one.").build();
        }
        userFromDB = repository.findByEmail(request.getEmail());
        if(userFromDB.isPresent()) {
            return (T) Message.builder().message("Email already exists! Try with another one.").build();
        }
        User user = User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .contactNumber(request.getContactNumber())
                        .role(request.getRole().toLowerCase() == "admin" ? Role.ADMIN : Role.USER)
                        .build();
        repository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return (T) AuthenticationResponse.builder()
                                        .token(jwtToken)
                                        .username(user.getUsername())
                                        .firstName(user.getFirstName())
                                        .lastName(user.getLastName())
                                        .email(user.getEmail())
                                        .contactNumber(user.getContactNumber())
                                        .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                                    .token(jwtToken)
                                    .username(user.getUsername())
                                    .firstName(user.getFirstName())
                                    .lastName(user.getLastName())
                                    .email(user.getEmail())
                                    .contactNumber(user.getContactNumber())
                                    .build();
    }

    public Message resetPassword(String username, Password password) {
        Optional<User> user = repository.findByUsername(username);
        if(!user.isPresent()) {
            return Message.builder().message("Username not found!").build();
        }

        user.ifPresent(userFromDB -> {
            userFromDB.setPassword(passwordEncoder.encode(password.getPassword()));
            repository.save(userFromDB);
        });
                        
        return Message.builder().message("Password reset successfull!").build();
    }

}
