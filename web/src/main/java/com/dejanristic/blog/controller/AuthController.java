package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(UrlMappings.LOGIN)
    public String loginForm() {
        return ViewNames.LOGIN;
    }

    @GetMapping(UrlMappings.REGISTER)
    public String registerForm(Model model) {
        if (!model.containsAttribute(AttributeNames.NEW_USER)) {
            model.addAttribute(AttributeNames.NEW_USER, new User());
        }

        return ViewNames.REGISTER;
    }

    @PostMapping(UrlMappings.REGISTER)
    public String register(
            @Valid @ModelAttribute(AttributeNames.NEW_USER) User user,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            return ViewNames.REGISTER;
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "error.username.exists", "Username already exists");
            return ViewNames.REGISTER;
        }

        userService.createUser(user);

        return UrlMappings.REDIRECT_HOME;
    }
}
