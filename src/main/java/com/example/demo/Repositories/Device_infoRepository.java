package com.example.demo.Repositories;

import com.example.demo.Entity.Device_info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Device_infoRepository extends JpaRepository<Device_info, Long> {
}
