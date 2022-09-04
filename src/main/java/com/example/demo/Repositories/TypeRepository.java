package com.example.demo.Repositories;

import com.example.demo.Entity.Brand;
import com.example.demo.Entity.Type;
import org.hibernate.type.ListType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    Type findByName(String name);
    Boolean existsByName(String name);

//    List<Type> findAllByBrands(List<Brand> brands);

}
