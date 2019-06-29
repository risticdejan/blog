package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.form.UserForm;
import com.dejanristic.blog.service.CategoryService;
import com.dejanristic.blog.service.FlashMessageService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.service.impl.UserSecurityService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.SecurityUtility;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final UserService userService;
    private final UserSecurityService userSecurityService;
    private final FlashMessageService flashMessageService;
    private final CategoryService categoryService;

    @Autowired
    public AuthController(
            UserService userService,
            UserSecurityService userSecurityService,
            FlashMessageService flashMessageService,
            CategoryService categoryService
    ) {
        this.userService = userService;
        this.userSecurityService = userSecurityService;
        this.flashMessageService = flashMessageService;
        this.categoryService = categoryService;
    }

    @ModelAttribute(AttributeNames.CATEGORIES)
    public List<Category> getCategories() {
        List<Category> categories = categoryService.findAll();
        return categories;
    }

    @GetMapping(UrlMappings.LOGIN)
    public String loginForm() {
        return ViewNames.LOGIN;
    }

    @GetMapping(UrlMappings.REGISTER)
    public String registerForm(Model model) {
        if (!model.containsAttribute(AttributeNames.NEW_USER)) {
            model.addAttribute(AttributeNames.NEW_USER, new UserForm());
        }

        return ViewNames.REGISTER;
    }

    @PostMapping(UrlMappings.REGISTER)
    public String register(
            @Valid @ModelAttribute(AttributeNames.NEW_USER) UserForm dataForm,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addUserToFlashAttribute(dataForm, result, redirectAttributes);

            return UrlMappings.REDIRECT_REGISTRER;
        }

        if (userService.findByUsername(dataForm.getUsername()) != null) {
            result.rejectValue("username", "error.username.exists", "Username already exists");

            addUserToFlashAttribute(dataForm, result, redirectAttributes);

            return UrlMappings.REDIRECT_REGISTRER;
        }

        User user = new User(
                dataForm.getUsername(),
                SecurityUtility.passwordEncoder().encode(dataForm.getPassword()),
                dataForm.getEmail()
        );

        User newUser = userService.createUser(user);

        if (userService.isItExists(newUser)) {

            UserDetails userDetails
                    = userSecurityService.loadUserByUsername(newUser.getUsername());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            flashMessageService
                    .userWasCreated(redirectAttributes);
        } else {
            flashMessageService
                    .errorWasHappend(redirectAttributes);
        }
        return UrlMappings.REDIRECT_HOME;
    }

    private void addUserToFlashAttribute(
            UserForm dataForm,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult." + AttributeNames.NEW_USER,
                result
        );
        redirectAttributes.addFlashAttribute(AttributeNames.NEW_USER, dataForm);
    }
}
