package com.example.demo.Repositories;

import com.example.demo.Entity.Type;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TypeRepository extends ReactiveCrudRepository<Type, Long> {

    Mono<Type> findByName(String name);
    Mono<Boolean> existsByName(String name);

//    List<Type> findAllByBrands(List<Brand> brands);

}
