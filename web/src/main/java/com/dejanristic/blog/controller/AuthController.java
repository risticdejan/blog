package com.dejanristic.blog.controller;

import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AuthController {

    @GetMapping(UrlMappings.LOGIN)
    public String loginForm() {
        log.info("Showing login page with form");
        return ViewNames.LOGIN;
    }

    @GetMapping(UrlMappings.REGISTER)
    public String registerForm(Model model) {
        log.info("Showing register page with form");
        return ViewNames.REGISTER;
    }
}
