package com.dejanristic.blog.controller.admin;

import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.Visitor;
import com.dejanristic.blog.domain.model.JsonRespone;
import com.dejanristic.blog.service.ArticleService;
import com.dejanristic.blog.service.DislikeArticleService;
import com.dejanristic.blog.service.LikeArticleService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.service.VisitorService;
import com.dejanristic.blog.service.impl.UserDetailsImpl;
import com.dejanristic.blog.util.admin.UrlAdminMappings;
import com.dejanristic.blog.util.admin.ViewAdminNames;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller("admin")
public class HomeController {

    private final LikeArticleService likeArticleService;
    private final DislikeArticleService dislikeArticleService;
    private final VisitorService visitorService;
    private final UserService userService;
    private final ArticleService articleService;

    @Autowired
    public HomeController(
            LikeArticleService likeArticleService,
            DislikeArticleService dislikeArticleService,
            VisitorService visitorService,
            UserService userService,
            ArticleService articleService
    ) {
        this.likeArticleService = likeArticleService;
        this.dislikeArticleService = dislikeArticleService;
        this.visitorService = visitorService;
        this.userService = userService;
        this.articleService = articleService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN)
    public String home(
            Authentication authentication,
            Model model
    ) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        int likesCount = likeArticleService.getAllLike().size();
        int dislikesCount = dislikeArticleService.getAllDislike().size();

        List<Visitor> visitorPerDay = visitorService.getAll();
        Long userCount = userService.count();
        Long articleCount = articleService.count();
        int max = visitorService.max();

        model.addAttribute("user", user);
        model.addAttribute("userCount", userCount);
        model.addAttribute("articleCount", articleCount);
        model.addAttribute("max", max);
        model.addAttribute("visitorPerDay", visitorPerDay);
        model.addAttribute("likesCount", likesCount);
        model.addAttribute("dislikesCount", dislikesCount);

        return ViewAdminNames.ADMIN;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(UrlAdminMappings.ADMIN)
    @ResponseBody
    public ResponseEntity<?> homeAjax(
            Authentication authentication,
            Model model
    ) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        int likesCount = likeArticleService.getAllLike().size();
        int dislikesCount = dislikeArticleService.getAllDislike().size();

        List<Visitor> visitorPerDay = visitorService.getAll();
        Long userCount = userService.count();
        Long articleCount = articleService.count();
        int max = visitorService.max();

        Map<String, Object> data = new HashMap();

        data.put("user", user);
        data.put("userCount", userCount);
        data.put("articleCount", articleCount);
        data.put("max", max);
        data.put("visitorPerDay", visitorPerDay);
        data.put("likesCount", likesCount);
        data.put("dislikesCount", dislikesCount);

        return new ResponseEntity(
                new JsonRespone("success", data),
                HttpStatus.OK
        );
    }
}
