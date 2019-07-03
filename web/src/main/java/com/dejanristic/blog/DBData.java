package com.dejanristic.blog;

import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.domain.Role;
import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.Visitor;
import com.dejanristic.blog.service.CategoryService;
import com.dejanristic.blog.service.RoleService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.service.VisitorService;
import com.dejanristic.blog.util.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBData {

    private final UserService userService;
    private final RoleService roleService;
    private final CategoryService categoryService;
    private final VisitorService visitorSerivce;

    @Autowired
    public DBData(
            UserService userService,
            RoleService roleService,
            CategoryService categoryService,
            VisitorService visitorSerivce
    ) {
        this.userService = userService;
        this.roleService = roleService;
        this.categoryService = categoryService;
        this.visitorSerivce = visitorSerivce;
    }

    public void load() {
        insertRoles();
        insertUsers();
        insertCategory();
        insertInitVisitor();
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

    private void insertInitVisitor() {
        visitorSerivce.create(new Visitor("Monday", 0));
        visitorSerivce.create(new Visitor("Tuesday", 0));
        visitorSerivce.create(new Visitor("Wednesday", 0));
        visitorSerivce.create(new Visitor("Thursday", 0));
        visitorSerivce.create(new Visitor("Friday", 0));
        visitorSerivce.create(new Visitor("Saturday", 0));
        visitorSerivce.create(new Visitor("Sunday", 0));
    }
}
