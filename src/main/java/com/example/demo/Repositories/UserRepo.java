package com.example.demo.Repositories;

import com.example.demo.Entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepo extends ReactiveCrudRepository<UserEntity,Long> {
    Mono<UserEntity> findByTelephoneNumber(String telephoneNumber);
    Mono<Boolean> existsByTelephoneNumber(String telephoneNumber);
}
