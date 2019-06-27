package com.dejanristic.blog.controller.admin;

import com.dejanristic.blog.annotation.PerPageAdmin;
import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.service.FlashMessageService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.SecurityUtility;
import com.dejanristic.blog.util.admin.UrlAdminMappings;
import com.dejanristic.blog.util.admin.ViewAdminNames;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class UserController {

    @Autowired
    @PerPageAdmin
    private int perPage;

    private final UserService userService;

    private final FlashMessageService flashMessageService;

    @Autowired
    public UserController(UserService userService, FlashMessageService flashMessageService) {
        this.userService = userService;
        this.flashMessageService = flashMessageService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_USER_LIST)
    public String index(
            @RequestParam(required = false) String page,
            Model model
    ) {

        int cleanPage = SecurityUtility.cleanPageParam(page);

        Page<User> users
                = userService.findAll(
                        "ROLE_USER",
                        PageRequest.of(cleanPage, perPage, Sort.by("id").descending())
                );

        model.addAttribute("users", users);
        return ViewAdminNames.ADMIN_USER_LIST;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_USER_SHOW + "/{id}")
    public String show(
            @PathVariable("id") String id,
            HttpServletRequest request,
            Model model
    ) {

        Long cleanId = SecurityUtility.cleanIdParam(id);

        User user = userService.findById(cleanId);

        String backUrl = (request.getHeader("Referer") != null)
                ? request.getHeader("Referer")
                : UrlAdminMappings.ADMIN_USER_LIST;

        model.addAttribute(AttributeNames.BACK_URL, backUrl);
        model.addAttribute("user", user);
        return ViewAdminNames.ADMIN_USER_SHOW;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_USER_BANNED + "/{id}")
    public String banned(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        userService.banned(cleanId);

        String backUrl = (request.getHeader("Referer") != null)
                ? request.getHeader("Referer")
                : UrlAdminMappings.ADMIN_USER_LIST;

        return "redirect:" + backUrl;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_USER_UNBANNED + "/{id}")
    public String unbanned(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        userService.unbanned(cleanId);

        String backUrl = (request.getHeader("Referer") != null)
                ? request.getHeader("Referer")
                : UrlAdminMappings.ADMIN_USER_LIST;

        return "redirect:" + backUrl;
    }
}
