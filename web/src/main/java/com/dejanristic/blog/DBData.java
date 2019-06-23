package com.dejanristic.blog;

import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.security.Role;
import com.dejanristic.blog.service.RoleService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.util.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBData {

    private UserService userService;

    private RoleService roleService;

    @Autowired
    public DBData(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public void load() {
        insertRoles();
        insertUsers();
    }

    private void insertRoles() {
        roleService.createRole(new Role(1L, "ROLE_USER"));
        roleService.createRole(new Role(2L, "ROLE_ADMIN"));
    }

    private void insertUsers() {
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
