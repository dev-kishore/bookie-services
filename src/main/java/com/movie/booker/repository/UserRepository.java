package com.movie.booker.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.movie.booker.dto.User;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {

    Optional<User> findByLoginIdAndPassword(String userId,String password);
    Optional<User> findByLoginId(String loginId);
    
}
