package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.validation.FormValidationGroup;
import com.dejanristic.blog.service.ArticleService;
import com.dejanristic.blog.service.FlashMessageService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.FlashNames;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ArticleController {

    private UserService userService;

    private ArticleService articleService;

    private FlashMessageService flashMessageService;

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
    public String createForm(Model model) {
        if (!model.containsAttribute(AttributeNames.NEW_ARTICLE)) {
            model.addAttribute(AttributeNames.NEW_ARTICLE, new Article());
        }
        return ViewNames.CREATE_ARTICLE_FORM;
    }

    @PostMapping(UrlMappings.ARTICLE_STORE)
    public String storeArticle(
            @Validated(FormValidationGroup.class) @ModelAttribute(AttributeNames.NEW_ARTICLE) Article article,
            BindingResult result,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult." + AttributeNames.NEW_ARTICLE,
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
            flashMessageService.flash(
                    FlashNames.ERROR_TYPE,
                    "Unfortunately, there was a problem, please try again later",
                    redirectAttributes
            );
        }
        return UrlMappings.REDIRECT_HOME;
    }
}
