package com.example.demo.Repositories;

import com.example.demo.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByTelephoneNumber(String telephoneNumber);
    Boolean existsByTelephoneNumber(String telephoneNumber);
}
