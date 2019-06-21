package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.service.FlashMessageService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.FlashNames;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class AuthController {

    private UserService userService;

    private FlashMessageService flashMessageService;

    @Autowired
    public AuthController(
            UserService userService,
            FlashMessageService flashMessageService
    ) {
        this.userService = userService;
        this.flashMessageService = flashMessageService;
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
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addUserToFlashAttribute(user, result, redirectAttributes);

            return UrlMappings.REDIRECT_REGISTRER;
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "error.username.exists", "Username already exists");

            addUserToFlashAttribute(user, result, redirectAttributes);

            return UrlMappings.REDIRECT_REGISTRER;
        }

        User newUser = userService.createUser(user);

        if (newUser != null) {
            flashMessageService.flash(
                    FlashNames.SUCCESS_TYPE,
                    "Thank you, You are registered successfully",
                    redirectAttributes
            );
        } else {
            flashMessageService.flash(
                    FlashNames.ERROR_TYPE,
                    "Sorry, some problem has happened",
                    redirectAttributes
            );
        }

        return UrlMappings.REDIRECT_HOME;
    }

    private void addUserToFlashAttribute(
            User user,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult." + AttributeNames.NEW_USER,
                result
        );
        redirectAttributes.addFlashAttribute(AttributeNames.NEW_USER, user);
    }
}
