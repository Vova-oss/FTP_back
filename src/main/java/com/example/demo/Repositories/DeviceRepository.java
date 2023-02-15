package com.example.demo.Repositories;

import com.example.demo.Entity.Device;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DeviceRepository extends ReactiveCrudRepository<Device, Long> {

    Mono<Boolean> existsByName(String name);
    Flux<Device> findAllByTypeIdAndBrandId(Long typeId, Long brandId);
    Flux<Device> findAllByTypeId(Long typeId);
    Flux<Device> findAllByBrandId(Long brandId);
    Mono<Device> findByName(String name);

}
