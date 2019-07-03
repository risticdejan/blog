package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.Role;
import com.dejanristic.blog.repository.RoleRepository;
import com.dejanristic.blog.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(String name) {
        return this.roleRepository.findByName(name);
    }

    @Override
    public Role findById(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Role createRole(Role r) {
        Role role = this.roleRepository.findByName(r.getName());

        if (role != null) {
            log.info("role {} already exists", r.getName());
        } else {
            role = this.roleRepository.save(r);
        }

        return role;
    }

    @Override
    public Role save(Role role) {
        return this.roleRepository.save(role);
    }

}
