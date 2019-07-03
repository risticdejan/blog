package com.dejanristic.blog.controller.admin;

import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.service.DislikeArticleService;
import com.dejanristic.blog.service.LikeArticleService;
import com.dejanristic.blog.service.impl.UserDetailsImpl;
import com.dejanristic.blog.util.admin.UrlAdminMappings;
import com.dejanristic.blog.util.admin.ViewAdminNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("admin")
public class HomeController {

    private final LikeArticleService likeArticleService;
    private final DislikeArticleService dislikeArticleService;

    @Autowired
    public HomeController(
            LikeArticleService likeArticleService,
            DislikeArticleService dislikeArticleService
    ) {
        this.likeArticleService = likeArticleService;
        this.dislikeArticleService = dislikeArticleService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN)
    public String home(
            Authentication authentication,
            Model model
    ) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        int likesCount = likeArticleService.getAllLike().size();
        int dislikesCount = dislikeArticleService.getAllDislike().size();

        model.addAttribute("user", user);
        model.addAttribute("likesCount", likesCount);
        model.addAttribute("dislikesCount", dislikesCount);

        return ViewAdminNames.ADMIN;
    }
}
