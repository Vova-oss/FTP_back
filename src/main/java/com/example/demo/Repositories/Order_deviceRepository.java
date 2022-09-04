package com.example.demo.Repositories;

import com.example.demo.Entity.Order;
import com.example.demo.Entity.Order_device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Order_deviceRepository extends JpaRepository<Order_device, Long> {

    List<Order_device> findAllByOrder(Order order);

}
