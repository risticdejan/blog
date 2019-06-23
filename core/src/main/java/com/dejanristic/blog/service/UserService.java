package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.User;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    User createUser(User user);

    User createAdmin(User admin);

    User save(User user);
}
