package com.dejanristic.blog;

import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.security.Role;
import com.dejanristic.blog.service.CategoryService;
import com.dejanristic.blog.service.RoleService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.util.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBData {

    private UserService userService;

    private RoleService roleService;

    private CategoryService categoryService;

    @Autowired
    public DBData(
            UserService userService,
            RoleService roleService,
            CategoryService categoryService
    ) {
        this.userService = userService;
        this.roleService = roleService;
        this.categoryService = categoryService;
    }

    public void load() {
        insertRoles();
        insertUsers();
        insertCategory();
    }

    private void insertRoles() {
        roleService.createRole(new Role("ROLE_USER"));
        roleService.createRole(new Role("ROLE_ADMIN"));
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

    private void insertCategory() {
        categoryService.create(new Category("php"));
        categoryService.create(new Category("java"));
        categoryService.create(new Category("javascript"));
        categoryService.create(new Category("sql"));
    }
}
