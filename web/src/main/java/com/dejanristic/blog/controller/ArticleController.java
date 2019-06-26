package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.validation.FormValidationGroup;
import com.dejanristic.blog.service.ArticleService;
import com.dejanristic.blog.service.FlashMessageService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.FlashNames;
import com.dejanristic.blog.util.PerPage;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import java.security.Principal;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
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

    @GetMapping(UrlMappings.ARTICLE_CREATE)
    public String create(Model model) {
        if (!model.containsAttribute(AttributeNames.NEW_ARTICLE)) {
            model.addAttribute(AttributeNames.NEW_ARTICLE, new Article());
        }
        return ViewNames.CREATE_ARTICLE_FORM;
    }

    @PostMapping(UrlMappings.ARTICLE_STORE)
    public String store(
            @Validated(FormValidationGroup.class)
            @ModelAttribute(AttributeNames.NEW_ARTICLE) Article article,
            BindingResult result,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult."
                    + AttributeNames.NEW_ARTICLE,
                    result
            );
            redirectAttributes.addFlashAttribute(AttributeNames.NEW_ARTICLE, article);

            return UrlMappings.REDIRECT_ARTICLE_CREATE;
        }

        User user = userService.findByUsername(principal.getName());

        article.setUser(user);

        article = articleService.createArticle(article);

        if (article != null) {
            flashMessageService.flash(
                    FlashNames.SUCCESS_TYPE,
                    "The article was created, as soon as possible "
                    + "it will be released",
                    redirectAttributes
            );
        } else {
            errorWasHappend(redirectAttributes);
        }
        return UrlMappings.REDIRECT_HOME;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(UrlMappings.ARTICLE_RELEASED_LIST)
    public String releasedArticlesList(
            @RequestParam(required = false) String page,
            Authentication authentication,
            Model model
    ) {
        int cleanPage = cleanPageParam(page);

        User user = (User) authentication.getPrincipal();

        Page<Article> articles
                = articleService.findAllReleasedArticlesByUser(
                        user.getId(),
                        PageRequest.of(cleanPage, perPage, Sort.by("publishedAt").descending())
                );

        model.addAttribute("articles", articles);
        return ViewNames.ARTICLE_RELEASED_LIST;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(UrlMappings.ARTICLE_UNRELEASED_LIST)
    public String unreleasedArticlesList(
            @RequestParam(required = false) String page,
            Authentication authentication,
            Model model
    ) {
        int cleanPage = cleanPageParam(page);

        User user = (User) authentication.getPrincipal();

        Page<Article> articles
                = articleService.findAllUnreleasedArticlesByUser(
                        user.getId(),
                        PageRequest.of(cleanPage, perPage, Sort.by("publishedAt").descending())
                );

        model.addAttribute("articles", articles);

        return ViewNames.ARTICLE_UNRELEASED_LIST;
    }

    @GetMapping(UrlMappings.ARTICLE_SHOW + "/{id}")
    public String show(
            @PathVariable("id") String id,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Long cleanId = cleanIdParam(id);

        Article article = articleService.findById(cleanId);
        if (article == null) {
            articleNotFound(redirectAttributes);
            return UrlMappings.REDIRECT_HOME;
        }

        model.addAttribute(AttributeNames.ARTICLE, article);
        return ViewNames.ARTICLE_SHOW;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(UrlMappings.ARTICLE_EDIT + "/{id}")
    public String edit(
            @PathVariable("id") String id,
            Model model,
            HttpServletRequest request,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        Long cleanId = cleanIdParam(id);

        Article article = articleService.findById(cleanId);

        User user = (User) authentication.getPrincipal();

        if (!Objects.equals(article.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("access forbidden");
        }

        if (article == null) {
            articleNotFound(redirectAttributes);
            return UrlMappings.REDIRECT_HOME;
        }

        model.addAttribute(AttributeNames.EDIT_ARTICLE, article);
        model.addAttribute("id", cleanId);

        return ViewNames.EDIT_ARTICLE_FORM;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(UrlMappings.ARTICLE_UPDATE + "/{id}")
    public String update(
            @Validated(FormValidationGroup.class)
            @ModelAttribute(AttributeNames.EDIT_ARTICLE) Article article,
            @PathVariable("id") String id,
            Authentication authentication,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        Long cleanId = cleanIdParam(id);

        article = articleService.update(cleanId, article);

        User user = (User) authentication.getPrincipal();

        if (!Objects.equals(article.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("access forbidden");
        }

        if (article != null) {
            flashMessageService.flash(
                    FlashNames.SUCCESS_TYPE,
                    "The article was updated",
                    redirectAttributes
            );
        } else {
            errorWasHappend(redirectAttributes);
        }

        return UrlMappings.REDIRECT_ARTICLE_UNRELEASED_LIST;
    }

    private void articleNotFound(RedirectAttributes redirectAttributes) {
        flashMessageService.flash(
                FlashNames.ERROR_TYPE,
                "Unfortunately, Article not found",
                redirectAttributes
        );
    }

    private void errorWasHappend(RedirectAttributes redirectAttributes) {
        flashMessageService.flash(
                FlashNames.ERROR_TYPE,
                "Unfortunately, there was a problem, "
                + "please try again later",
                redirectAttributes
        );
    }

    private Long cleanIdParam(String id) {
        Long cleanId;
        try {
            id = (id == null) ? "1" : id;
            cleanId = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            cleanId = 0L;
        }

        return cleanId;
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
