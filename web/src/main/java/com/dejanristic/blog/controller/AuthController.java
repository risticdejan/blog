package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.validation.FormValidationGroup;
import com.dejanristic.blog.service.FlashMessageService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.service.impl.UserSecurityService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.SecurityUtility;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class AuthController {

    private UserService userService;

    private UserSecurityService userSecurityService;

    private FlashMessageService flashMessageService;

    @Autowired
    public AuthController(
            UserService userService,
            UserSecurityService userSecurityService,
            FlashMessageService flashMessageService
    ) {
        this.userService = userService;
        this.userSecurityService = userSecurityService;
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
            @Validated(FormValidationGroup.class) @ModelAttribute(AttributeNames.NEW_USER) User user,
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

        user.setPassword(
                SecurityUtility.passwordEncoder().encode(user.getPassword())
        );

        User newUser = userService.createUser(user);

        UserDetails userDetails
                = userSecurityService.loadUserByUsername(newUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (userService.isItExists(newUser)) {
            flashMessageService
                    .userWasCreated(redirectAttributes);
        } else {
            flashMessageService
                    .errorWasHappend(redirectAttributes);
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
