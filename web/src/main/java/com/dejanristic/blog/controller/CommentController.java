package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.domain.Comment;
import com.dejanristic.blog.domain.model.JsonRespone;
import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.form.CommentForm;
import com.dejanristic.blog.service.ArticleService;
import com.dejanristic.blog.service.CommentService;
import com.dejanristic.blog.service.impl.UserDetailsImpl;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.SecurityUtility;
import com.dejanristic.blog.util.UrlMappings;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CommentController {

    private final CommentService commentService;
    private final ArticleService articleService;

    @Autowired
    public CommentController(
            CommentService commentService,
            ArticleService articleService
    ) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(UrlMappings.COMMENT_STORE + "/{id}")
    public ResponseEntity<?> store(
            @PathVariable("id") String id,
            @Valid @ModelAttribute(AttributeNames.NEW_COMMENT) CommentForm formData,
            BindingResult result,
            Authentication authentication
    ) {
        if (result.hasErrors()) {

            Map<String, String> errors = new HashMap();
            for (FieldError fe : result.getFieldErrors()) {
                if (!errors.containsKey(fe.getField())) {
                    errors.put(fe.getField(), fe.getDefaultMessage());
                }
                System.out.println(fe.getDefaultMessage());
            }

            return new ResponseEntity(
                    new JsonRespone("failed", errors),
                    HttpStatus.OK
            );
        }

        Long cleanId = SecurityUtility.cleanIdParam(id);
        Article article = articleService.findById(cleanId);
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        Comment comment = new Comment(formData.getBody());
        comment.setArticle(article);
        comment.setUser(user);

        comment = commentService.create(comment);

        return new ResponseEntity(
                new JsonRespone("success", comment),
                HttpStatus.OK
        );
    }
}
