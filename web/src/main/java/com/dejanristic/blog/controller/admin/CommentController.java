package com.dejanristic.blog.controller.admin;

import com.dejanristic.blog.annotation.PerPageAdmin;
import com.dejanristic.blog.domain.Comment;
import com.dejanristic.blog.service.CommentService;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller("admin.comment")
public class CommentController {

    @Autowired
    @PerPageAdmin
    private int perPage;

    private CommentService commentService;
    private final Flash flash;

    @Autowired
    public CommentController(
            CommentService commentService,
            Flash flash
    ) {
        this.commentService = commentService;
        this.flash = flash;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_COMMENT_LIST)
    public String index(
            @RequestParam(required = false) String page,
            Model model
    ) {
        int cleanPage = SecurityUtility.cleanPageParam(page);

        Page<Comment> comments
                = commentService.findAll(
                        PageRequest.of(cleanPage, perPage, Sort.by("createdAt").descending())
                );

        model.addAttribute("comments", comments);

        return ViewAdminNames.ADMIN_COMMENT_LIST;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_COMMENT_SHOW + "/{id}")
    public String show(
            @PathVariable("id") String id,
            HttpServletRequest request,
            Model model
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Comment comment = commentService.findById(cleanId);

        String backUrl = (request.getHeader("Referer") != null)
                ? request.getHeader("Referer")
                : UrlAdminMappings.ADMIN;

        model.addAttribute("comment", comment);
        model.addAttribute(AttributeNames.BACK_URL, backUrl);

        return ViewAdminNames.ADMIN_COMMENT_SHOW;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_COMMENT_DELETE + "/{id}")
    public String delete(
            @PathVariable("id") String id,
            HttpServletRequest request,
            Model model
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Comment comment = commentService.findById(cleanId);

        if (comment != null) {
            commentService.delete(comment);

            flash.success("The comment was deleted");
        } else {
            flash.error("Unfortunately, there was a problem, "
                    + "please try again later");
        }

        String backUrl = (request.getHeader("Referer") != null)
                ? request.getHeader("Referer")
                : UrlAdminMappings.ADMIN;

        return "redirect:" + backUrl;
    }
}
