package com.example.hoya.services;

import com.example.hoya.entities.Role;
import com.example.hoya.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findById(Long id) {

        return roleRepository.findRoleById(id);
    }
}
