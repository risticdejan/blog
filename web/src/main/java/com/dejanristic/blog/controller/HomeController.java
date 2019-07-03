package com.dejanristic.blog.controller;

import com.dejanristic.blog.annotation.PerPage;
import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.service.ArticleService;
import com.dejanristic.blog.service.CategoryService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.SecurityUtility;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final CategoryService categoryService;

    @Autowired
    public HomeController(
            ArticleService articleService,
            CategoryService categoryService
    ) {
        this.articleService = articleService;
        this.categoryService = categoryService;
    }

    @ModelAttribute(AttributeNames.CATEGORIES)
    public List<Category> getCategories() {
        List<Category> categories = categoryService.findAll();
        return categories;
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
        System.out.println("home test");
        int cleanPage = SecurityUtility.cleanPageParam(page);

        Page<Article> articles
                = articleService.findAllReleasedArticles(
                        PageRequest.of(cleanPage, perPage, Sort.by("publishedAt").descending())
                );

        model.addAttribute("articles", articles);
        return ViewNames.HOME;
    }

    @RequestMapping("*")
    public String notFoundPage() {
        return ViewNames.ERROR_404;
    }
}
