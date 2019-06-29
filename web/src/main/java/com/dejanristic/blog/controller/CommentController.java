package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.domain.Comment;
import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.form.CommentForm;
import com.dejanristic.blog.service.ArticleService;
import com.dejanristic.blog.service.CategoryService;
import com.dejanristic.blog.service.CommentService;
import com.dejanristic.blog.service.FlashMessageService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.SecurityUtility;
import com.dejanristic.blog.util.UrlMappings;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class CommentController {

    private final CategoryService categoryService;
    private final CommentService commentService;
    private final ArticleService articleService;
    private final FlashMessageService flashMessageService;

    @Autowired
    public CommentController(
            CategoryService categoryService,
            CommentService commentService,
            ArticleService articleService,
            FlashMessageService flashMessageService
    ) {
        this.categoryService = categoryService;
        this.commentService = commentService;
        this.articleService = articleService;
        this.flashMessageService = flashMessageService;
    }

    @ModelAttribute(AttributeNames.CATEGORIES)
    public List<Category> getCategories() {
        List<Category> categories = categoryService.findAll();
        return categories;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(UrlMappings.COMMENT_STORE + "/{id}")
    public String store(
            @PathVariable("id") String id,
            @Valid @ModelAttribute(AttributeNames.NEW_COMMENT) CommentForm formData,
            BindingResult result,
            Model model,
            HttpServletRequest request,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult."
                    + AttributeNames.NEW_COMMENT,
                    result
            );
            redirectAttributes.addFlashAttribute(AttributeNames.NEW_COMMENT, formData);

            return UrlMappings.REDIRECT_ARTICLE_SHOW + "/" + cleanId;
        }

        Article article = articleService.findById(cleanId);
        User user = (User) authentication.getPrincipal();

        Comment comment = new Comment(formData.getBody());
        comment.setArticle(article);
        comment.setUser(user);

        comment = commentService.create(comment);

        return UrlMappings.REDIRECT_ARTICLE_SHOW
                + "/" + cleanId + "#comment-area";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(UrlMappings.COMMENT_REMOVE + "/{id}/{aid}")
    public String remove(
            @PathVariable("id") String id,
            @PathVariable("aid") String aid,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);
        Long cleanAid = SecurityUtility.cleanIdParam(aid);

        Comment comment = commentService.findById(cleanId);

        if (comment != null) {
            User user = (User) authentication.getPrincipal();

            if (!Objects.equals(comment.getUser().getId(), user.getId())) {
                throw new AccessDeniedException("access forbidden");
            }

            commentService.delete(comment);

            flashMessageService
                    .commentWasDeleted(redirectAttributes);
        } else {
            flashMessageService
                    .errorWasHappend(redirectAttributes);
        }

        return UrlMappings.REDIRECT_ARTICLE_SHOW
                + "/" + cleanAid + "#comment-area";
    }

}
