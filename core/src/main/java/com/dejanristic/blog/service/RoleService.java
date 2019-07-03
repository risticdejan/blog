package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.Role;

public interface RoleService {

    Role findByName(String name);

    Role findById(Long id);

    Role createRole(Role role);

    Role save(Role role);
}
