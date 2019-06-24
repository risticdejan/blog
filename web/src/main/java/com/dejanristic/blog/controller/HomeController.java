package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.service.ArticleService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.PerPage;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    @PerPage
    private int perPage;

    private ArticleService articleService;

    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

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
    public String home(
            @RequestParam(required = false) String page,
            Model model
    ) {
        int cleanPage;
        try {
            page = (page == null) ? "1" : page;
            cleanPage = Integer.parseInt(page) - 1;
        } catch (NumberFormatException ex) {
            cleanPage = 0;
        }

        Page<Article> articles
                = articleService.findAll(
                        PageRequest.of(cleanPage, perPage, Sort.by("publishedAt").descending())
                );

        model.addAttribute("articles", articles);
        return ViewNames.HOME;
    }

    @RequestMapping("*")
    public String notFoundPage() {
        return "error/404";
    }
}
