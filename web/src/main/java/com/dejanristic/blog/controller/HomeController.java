package com.dejanristic.blog.controller;

import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @ModelAttribute(AttributeNames.CURRENT_USER)
    public UserDetails getCurrentUser(Authentication authentication) {
        return (authentication == null)
                ? null : (UserDetails) authentication.getPrincipal();
    }

    @GetMapping("/")
    public String index() {
        return UrlMappings.REDIRECT_HOME;
    }

    @GetMapping(UrlMappings.HOME)
    public String home() {
        return ViewNames.HOME;
    }

    @RequestMapping("*")
    public String notFoundPage() {
        return "error/404";
    }
}
