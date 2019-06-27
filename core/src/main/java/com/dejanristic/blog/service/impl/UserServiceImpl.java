package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.security.Role;
import com.dejanristic.blog.repository.RoleRepository;
import com.dejanristic.blog.repository.UserRepository;
import com.dejanristic.blog.service.UserService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean isItExists(User user) {
        return user != null;
    }

    @Override
    public Page<User> findAll(String name, Pageable pageable) {
        return userRepository.findByRolesName(name, pageable);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        User localUser = userRepository.findByUsername(user.getUsername());

        if (localUser != null) {
            log.info("user {} already exists. Nothing will be done.", user.getUsername());
        } else {

            Role role = roleRepository.findByName("ROLE_USER");

            user.addRole(role);

            user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            localUser = userRepository.save(user);
        }

        return localUser;
    }

    @Override
    @Transactional
    public User createAdmin(User admin) {
        User localUser = userRepository.findByUsername(admin.getUsername());

        if (localUser != null) {
            log.info("user {} already exists. Nothing will be done.", admin.getUsername());
        } else {

            Role role = roleRepository.findByName("ROLE_ADMIN");
            admin.addRole(role);

            admin.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            admin.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            localUser = userRepository.save(admin);

        }

        return localUser;
    }

    @Override
    public User banned(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            user.setEnabled(false);
            return userRepository.save(user);
        }
        return user;
    }

    @Override
    public User unbanned(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            user.setEnabled(true);
            return userRepository.save(user);
        }
        return user;
    }
}
