package com.example.demo.Repositories;

import com.example.demo.Entity.Brand;
import com.example.demo.Entity.Device;
import com.example.demo.Entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Boolean existsByName(String name);
    List<Device> findAllByTypeIdAndBrandId(Type typeId, Brand brandId);
    List<Device> findAllByTypeId(Type typeId);
    List<Device> findAllByBrandId(Brand brandId);
    Device findByName(String name);

}
