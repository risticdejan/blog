package com.dejanristic.blog.controller.admin;

import com.dejanristic.blog.annotation.PerPageAdmin;
import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.service.ArticleService;
import com.dejanristic.blog.service.Flash;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller("admin.article")
public class ArticleController {

    @Autowired
    @PerPageAdmin
    private int perPage;

    private final ArticleService articleService;
    private final Flash flash;

    @Autowired
    public ArticleController(
            ArticleService articleService,
            Flash flash
    ) {
        this.articleService = articleService;
        this.flash = flash;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_ARTICLE_SHOW + "/{id}")
    public String show(
            @PathVariable("id") String id,
            HttpServletRequest request,
            Model model
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Article article = articleService.findById(cleanId);

        String backUrl = (request.getHeader("Referer") != null)
                ? request.getHeader("Referer")
                : UrlAdminMappings.ADMIN_UNRELEASED_ARTICLES_LIST;

        model.addAttribute(AttributeNames.ARTICLE, article);
        model.addAttribute(AttributeNames.BACK_URL, backUrl);

        return ViewAdminNames.ADMIN_ARTICLE_SHOW;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_RELEASED_ARTICLES_LIST)
    public String releasedArticlesList(
            @RequestParam(required = false) String page,
            Model model
    ) {
        int cleanPage = SecurityUtility.cleanPageParam(page);

        Page<Article> articles
                = articleService.findAllReleasedArticles(
                        PageRequest.of(cleanPage, perPage, Sort.by("publishedAt").descending())
                );

        model.addAttribute("articles", articles);

        return ViewAdminNames.ADMIN_ARTICLES_LIST;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_UNRELEASED_ARTICLES_LIST)
    public String unreleasedArticlesList(
            @RequestParam(required = false) String page,
            Model model
    ) {
        int cleanPage = SecurityUtility.cleanPageParam(page);

        Page<Article> articles
                = articleService.findAllUnreleasedArticles(
                        PageRequest.of(cleanPage, perPage, Sort.by("id").descending())
                );

        model.addAttribute("articles", articles);

        return ViewAdminNames.ADMIN_ARTICLES_UNRELEASED_LIST;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_ARTICLE_DELETE + "/{id}")
    public String delete(
            @PathVariable("id") String id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Article article = articleService.findById(cleanId);

        if (articleService.isItExists(article)) {
            articleService.delete(article);

            flash.success("The article was deleted");
        } else {
            flash.error("Unfortunately, there was a problem, "
                    + "please try again later");
        }

        return UrlAdminMappings.REDIRECT_ADMIN_UNRELEASED_ARTICLES_LIST;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_ARTICLE_RELEASE + "/{id}")
    public String release(
            @PathVariable("id") String id,
            HttpServletRequest request,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Article article = articleService.findById(cleanId);

        if (articleService.isItExists(article)) {
            articleService.release(article);

            flash.success("Article has released");
        } else {
            flash.error("Unfortunately, there was a problem, "
                    + "please try again later");
        }

        String backUrl = (request.getHeader("Referer") != null)
                ? request.getHeader("Referer")
                : UrlAdminMappings.ADMIN_UNRELEASED_ARTICLES_LIST;

        return "redirect:" + backUrl;
    }
}
