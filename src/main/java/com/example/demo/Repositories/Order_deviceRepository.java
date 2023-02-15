package com.example.demo.Repositories;

import com.example.demo.Entity.Order;
import com.example.demo.Entity.Order_device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface Order_deviceRepository extends ReactiveCrudRepository<Order_device, Long> {

    Flux<Order_device> findAllByOrderId(Long orderId);

}
