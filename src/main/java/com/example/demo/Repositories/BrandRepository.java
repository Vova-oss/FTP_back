package com.example.demo.Repositories;

import com.example.demo.Entity.Brand;
import com.example.demo.Entity.Type;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BrandRepository extends ReactiveCrudRepository<Brand, Long> {

    Mono<Brand> findByNameAndTypeId(String name, Long typeId);
    Flux<Brand> findAllByTypeId(Long typeId);

    Mono<Void> deleteByTypeId(Long id);
}
