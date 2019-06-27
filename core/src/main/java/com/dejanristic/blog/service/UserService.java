package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    boolean isItExists(User user);

    Page<User> findAll(String name, Pageable pageable);

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    User createUser(User user);

    User createAdmin(User admin);

    User save(User user);

    User banned(Long id);

    User unbanned(Long id);
}
