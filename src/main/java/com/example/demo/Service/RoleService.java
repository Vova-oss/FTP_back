package com.example.demo.Service;

import com.example.demo.Entity.Enum.ERoles;
import com.example.demo.Entity.Role;
import com.example.demo.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    /**
     * Получение сущности Role по её названию (ERoles)
     * @param role enum ERoles
     */
    public Role findByRole(ERoles role) {
        return roleRepository.findByRole(role);
    }
}
