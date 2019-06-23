package com.dejanristic.blog;

import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.security.Role;
import com.dejanristic.blog.service.RoleService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.util.SecurityUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BlogApplication implements CommandLineRunner {

    private UserService userService;

    private RoleService roleService;

    @Autowired
    public BlogApplication(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "/blog");
        SpringApplication.run(BlogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        roleService.createRole(new Role(1L, "ROLE_USER"));
        roleService.createRole(new Role(2L, "ROLE_ADMIN"));

        userService.createUser(new User("dejan",
                SecurityUtility.passwordEncoder().encode("dejan"),
                "dejan@test.com"
        ));
        userService.createAdmin(new User("admin",
                SecurityUtility.passwordEncoder().encode("admin"),
                "admin@test.com"
        ));

    }

}
