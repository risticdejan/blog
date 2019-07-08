package com.dejanristic.blog.controller.admin;

import com.dejanristic.blog.annotation.PerPageAdmin;
import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.domain.Comment;
import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.model.JsonRespone;
import com.dejanristic.blog.service.ArticleService;
import com.dejanristic.blog.service.CommentService;
import com.dejanristic.blog.service.Flash;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class UserController {

    @Autowired
    @PerPageAdmin
    private int perPage;

    private final UserService userService;
    private final ArticleService articleService;
    private final CommentService commentService;
    private final Flash flash;

    @Autowired
    public UserController(
            UserService userService,
            ArticleService articleService,
            CommentService commetService,
            Flash flash
    ) {
        this.userService = userService;
        this.articleService = articleService;
        this.commentService = commetService;
        this.flash = flash;
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
            @RequestParam(required = false) String apage,
            @RequestParam(required = false) String cpage,
            HttpServletRequest request,
            Model model
    ) {

        Long cleanId = SecurityUtility.cleanIdParam(id);
        int cleanAPage = SecurityUtility.cleanPageParam(apage);
        int cleanCPage = SecurityUtility.cleanPageParam(cpage);

        User user = userService.findById(cleanId);
        Page<Article> articles = articleService.findAllArticlesByUser(
                user.getId(),
                PageRequest.of(cleanAPage, perPage, Sort.by("publishedAt").descending())
        );
        Page<Comment> comments = commentService.findAllCommentsByUser(
                user.getId(),
                PageRequest.of(cleanCPage, perPage, Sort.by("createdAt").descending())
        );

        String backUrl = (request.getHeader("Referer") != null)
                ? request.getHeader("Referer")
                : UrlAdminMappings.ADMIN_USER_LIST;

        model.addAttribute(AttributeNames.BACK_URL, backUrl);
        model.addAttribute("user", user);
        model.addAttribute("articles", articles);
        model.addAttribute("comments", comments);
        return ViewAdminNames.ADMIN_USER_SHOW;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(UrlAdminMappings.ADMIN_USER_BANNED + "/{id}")
    @ResponseBody
    public ResponseEntity<?> banned(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        User user = userService.banned(cleanId);

        if (user != null) {
            flash.error("User is disabled");
        } else {
            flash.error("Unfortunately, there was a problem, "
                    + "please try again later");
        }

        return new ResponseEntity(
                new JsonRespone("success", "user banned"),
                HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(UrlAdminMappings.ADMIN_USER_UNBANNED + "/{id}")
    @ResponseBody
    public ResponseEntity<?> unbanned(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        User user = userService.unbanned(cleanId);

        if (user != null) {
            flash.success("User is enabled");
        } else {
            flash.error("Unfortunately, there was a problem, "
                    + "please try again later");
        }

        return new ResponseEntity(
                new JsonRespone("success", "user unbanned"),
                HttpStatus.OK
        );
    }
}
