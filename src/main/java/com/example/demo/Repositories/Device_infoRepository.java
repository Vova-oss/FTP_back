package com.example.demo.Repositories;

import com.example.demo.Entity.Device_info;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface Device_infoRepository extends ReactiveCrudRepository<Device_info, Long> {
    Flux<Device_info> findAllByDeviceId(Long deviceId);
}
