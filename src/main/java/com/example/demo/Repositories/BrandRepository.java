package com.example.demo.Repositories;

import com.example.demo.Entity.Brand;
import com.example.demo.Entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    Brand findByNameAndTypeId(String name, Type typeId);

}
