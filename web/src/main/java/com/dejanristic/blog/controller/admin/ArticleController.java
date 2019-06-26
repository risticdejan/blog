package com.dejanristic.blog.controller.admin;

import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.service.ArticleService;
import com.dejanristic.blog.service.FlashMessageService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.util.PerPage;
import com.dejanristic.blog.util.UrlAdminMappings;
import com.dejanristic.blog.util.ViewAdminNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller("admin.article")
public class ArticleController {

    @Autowired
    @PerPage
    private int perPage;

    private final UserService userService;

    private final ArticleService articleService;

    private final FlashMessageService flashMessageService;

    @Autowired
    public ArticleController(
            UserService userService,
            ArticleService articleService,
            FlashMessageService flashMessageService
    ) {
        this.userService = userService;
        this.articleService = articleService;
        this.flashMessageService = flashMessageService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_ARTICLES_LIST)
    public String articles(
            @RequestParam(required = false) String page,
            Model model
    ) {
        int cleanPage = cleanPageParam(page);

        Page<Article> articles
                = articleService.findAllReleasedArticles(
                        PageRequest.of(cleanPage, perPage, Sort.by("publishedAt").descending())
                );

        model.addAttribute("articles", articles);

        return ViewAdminNames.ADMIN_ARTICLES_LIST;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_ARTICLES_UNRELEASED_LIST)
    public String releasingList(
            @RequestParam(required = false) String page,
            Model model
    ) {
        int cleanPage = cleanPageParam(page);

        Page<Article> articles
                = articleService.findAllUnreleasedArticles(
                        PageRequest.of(cleanPage, perPage, Sort.by("id").descending())
                );

        model.addAttribute("articles", articles);

        return ViewAdminNames.ADMIN_ARTICLES_UNRELEASED_LIST;
    }

    private int cleanPageParam(String page) {
        int cleanPage;
        try {
            page = (page == null) ? "1" : page;
            cleanPage = Integer.parseInt(page) - 1;
        } catch (NumberFormatException ex) {
            cleanPage = 0;
        }

        return cleanPage;
    }
}
