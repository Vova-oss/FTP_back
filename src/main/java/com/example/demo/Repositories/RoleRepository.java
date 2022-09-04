package com.example.demo.Repositories;

import com.example.demo.Entity.Enum.ERoles;
import com.example.demo.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(ERoles role);
}
