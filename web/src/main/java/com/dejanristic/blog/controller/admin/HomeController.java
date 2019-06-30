package com.dejanristic.blog.controller.admin;

import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.service.impl.UserDetailsImpl;
import com.dejanristic.blog.util.admin.UrlAdminMappings;
import com.dejanristic.blog.util.admin.ViewAdminNames;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("admin")
public class HomeController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN)
    public String home(
            Authentication authentication,
            Model model
    ) {

        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        model.addAttribute("user", user);
        return ViewAdminNames.ADMIN;
    }
}
