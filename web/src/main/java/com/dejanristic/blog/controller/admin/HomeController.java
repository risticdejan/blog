package com.dejanristic.blog.controller.admin;

import com.dejanristic.blog.util.UrlAdminMappings;
import com.dejanristic.blog.util.ViewAdminNames;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("admin")
public class HomeController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN)
    public String home() {
        return ViewAdminNames.ADMIN;
    }
}
