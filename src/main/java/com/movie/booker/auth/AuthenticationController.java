package com.movie.booker.auth;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.booker.dto.Message;
import com.movie.booker.dto.Password;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://bookie-angular-app.s3-website-us-east-1.amazonaws.com")
@RestController
@RequestMapping("/api/v1.0/moviebooking/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PutMapping("/{username}/forgot")
    public ResponseEntity<Message> resetPassword(@NonNull @PathVariable String username, @Valid @RequestBody Password password) {
        return ResponseEntity.ok(authenticationService.resetPassword(username, password));
    }
    
}
