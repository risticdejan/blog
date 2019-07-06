package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.form.LoginForm;
import com.dejanristic.blog.domain.form.RegisterForm;
import com.dejanristic.blog.domain.model.JsonRespone;
import com.dejanristic.blog.service.CategoryService;
import com.dejanristic.blog.service.Flash;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.service.impl.UserSecurityService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.SecurityUtility;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class AuthController {

    private final UserService userService;
    private final UserSecurityService userSecurityService;
    private final CategoryService categoryService;
    private final Flash flash;

    @Autowired
    public AuthController(
            UserService userService,
            UserSecurityService userSecurityService,
            CategoryService categoryService,
            Flash flash
    ) {
        this.userService = userService;
        this.userSecurityService = userSecurityService;
        this.categoryService = categoryService;
        this.flash = flash;
    }

    @ModelAttribute(AttributeNames.CATEGORIES)
    public List<Category> getCategories() {
        List<Category> categories = categoryService.findAll();
        return categories;
    }

    @GetMapping(UrlMappings.LOGIN)
    public String loginForm(Model model) {
        if (!model.containsAttribute(AttributeNames.LOGIN_USER)) {
            model.addAttribute(AttributeNames.LOGIN_USER, new LoginForm());
        }

        return ViewNames.LOGIN;
    }

    @PostMapping(UrlMappings.LOGIN)
    @ResponseBody
    public ResponseEntity<?> login(
            @Valid @ModelAttribute(AttributeNames.LOGIN_USER) LoginForm dataForm,
            BindingResult result,
            HttpServletRequest request
    ) {

        String path = request.getContextPath();
        Map<String, Object> data = new HashMap();
        Map<String, String> errors = new HashMap();

        if (result.hasErrors()) {
            for (FieldError fe : result.getFieldErrors()) {
                if (!errors.containsKey(fe.getField())) {
                    errors.put(fe.getField(), fe.getDefaultMessage());
                }
            }

            return new ResponseEntity(
                    new JsonRespone("failed", errors),
                    HttpStatus.OK
            );
        }

        User user = this.userService.findByUsername(dataForm.getUsername());

        if (SecurityUtility.passwordEncoder().matches(dataForm.getPassword(), user.getPassword())) {

            UserDetails userDetails
                    = userSecurityService.loadUserByUsername(user.getUsername());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            data.put("url", path + UrlMappings.HOME);

            flash.success("Now, You are logged successfully");

            return new ResponseEntity(
                    new JsonRespone("success", data),
                    HttpStatus.OK
            );
        }

        errors.put("username", "Incorrect username or password");

        return new ResponseEntity(
                new JsonRespone("failed", errors),
                HttpStatus.OK
        );
    }

    @GetMapping(UrlMappings.REGISTER)
    public String registerForm(Model model) {
        if (!model.containsAttribute(AttributeNames.NEW_USER)) {
            model.addAttribute(AttributeNames.NEW_USER, new RegisterForm());
        }

        return ViewNames.REGISTER;
    }

    @PostMapping(UrlMappings.REGISTER)
    @ResponseBody
    public ResponseEntity<?> register(
            @Valid @ModelAttribute(AttributeNames.NEW_USER) RegisterForm dataForm,
            BindingResult result,
            HttpServletRequest request
    ) {
        String path = request.getContextPath();

        Map<String, Object> data = new HashMap();

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap();
            for (FieldError fe : result.getFieldErrors()) {
                if (!errors.containsKey(fe.getField())) {
                    errors.put(fe.getField(), fe.getDefaultMessage());
                }
            }

            return new ResponseEntity(
                    new JsonRespone("failed", errors),
                    HttpStatus.OK
            );
        }

        if (userService.findByUsername(dataForm.getUsername()) != null) {
            Map<String, String> errors = new HashMap();
            errors.put("username", "Username already exists");

            return new ResponseEntity(
                    new JsonRespone("failed", errors),
                    HttpStatus.OK
            );
        }

        User user = new User(
                dataForm.getUsername(),
                SecurityUtility.passwordEncoder().encode(dataForm.getPassword()),
                dataForm.getEmail(),
                dataForm.getFirstname(),
                dataForm.getLastname()
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

            flash.success("Thank you, "
                    + "You are registered successfully");
        } else {
            flash.error("Unfortunately, there was a problem, "
                    + "please try again later");
        }

        data.put("url", path + UrlMappings.HOME);

        return new ResponseEntity(
                new JsonRespone("success", data),
                HttpStatus.OK
        );
    }
}
