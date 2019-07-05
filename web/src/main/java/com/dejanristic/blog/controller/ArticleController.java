package com.dejanristic.blog.controller;

import com.dejanristic.blog.annotation.PerPage;
import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.domain.DislikeArticle;
import com.dejanristic.blog.domain.LikeArticle;
import com.dejanristic.blog.domain.User;
import com.dejanristic.blog.domain.form.ArticleForm;
import com.dejanristic.blog.domain.form.CommentForm;
import com.dejanristic.blog.domain.model.JsonRespone;
import com.dejanristic.blog.exception.ArticleAlreadyExists;
import com.dejanristic.blog.exception.ArticleNotFound;
import com.dejanristic.blog.service.ArticleService;
import com.dejanristic.blog.service.CategoryService;
import com.dejanristic.blog.service.CommentService;
import com.dejanristic.blog.service.DislikeArticleService;
import com.dejanristic.blog.service.Flash;
import com.dejanristic.blog.service.LikeArticleService;
import com.dejanristic.blog.service.UserService;
import com.dejanristic.blog.service.impl.UserDetailsImpl;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.SecurityUtility;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class ArticleController {

    @Autowired
    @PerPage
    private int perPage;

    private final UserService userService;
    private final ArticleService articleService;
    private final CommentService commentService;
    private final CategoryService categoryService;
    private final LikeArticleService likeArticleService;
    private final DislikeArticleService dislikeArticleService;
    private final Flash flash;

    @Autowired
    public ArticleController(
            UserService userService,
            ArticleService articleService,
            CommentService commentSerivce,
            CategoryService categoryService,
            LikeArticleService likeArticleService,
            DislikeArticleService dislikeArticleService,
            Flash flash
    ) {
        this.userService = userService;
        this.articleService = articleService;
        this.commentService = commentSerivce;
        this.categoryService = categoryService;
        this.likeArticleService = likeArticleService;
        this.dislikeArticleService = dislikeArticleService;
        this.flash = flash;
    }

    @ModelAttribute(AttributeNames.CATEGORIES)
    public List<Category> getCategories() {
        List<Category> categories = categoryService.findAll();
        return categories;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(UrlMappings.ARTICLE_CREATE)
    public String create(Model model) {
        if (!model.containsAttribute(AttributeNames.NEW_ARTICLE)) {
            model.addAttribute(AttributeNames.NEW_ARTICLE, new ArticleForm());
        }
        return ViewNames.CREATE_ARTICLE_FORM;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(UrlMappings.ARTICLE_STORE)
    @ResponseBody
    public ResponseEntity<?> store(
            @Valid @ModelAttribute(AttributeNames.NEW_ARTICLE) ArticleForm formData,
            BindingResult result,
            Principal principal,
            HttpServletRequest request,
            Model model
    ) {
        String path = request.getContextPath();

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap();
            for (FieldError fe : result.getFieldErrors()) {
                if (!errors.containsKey(fe.getField())) {
                    errors.put(fe.getField(), fe.getDefaultMessage());
                }
            }

            return new ResponseEntity(
                    new JsonRespone("failed", errors),
                    HttpStatus.OK
            );
        }

        Article article
                = new Article(formData.getTitle(), formData.getDescription(), formData.getBody());
        article.setUser(userService.findByUsername(principal.getName()));
        article.setCategory(categoryService.findById(formData.getCategoryId()));

        Map<String, Object> data = new HashMap();
        try {

            article = articleService.create(article);

            MultipartFile image = formData.getImage();

            try {
                byte[] bytes = image.getBytes();
                String name = article.getId() + ".jpg";
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(new File("src/main/resources/static/img/article/" + name)));
                stream.write(bytes);
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (articleService.isItExists(article)) {
                flash.success("The article was created, as soon as possible "
                        + "it will be released");
            } else {
                flash.error("Unfortunately, there was a problem, "
                        + "please try again later");
            }
            data.put("url", path + UrlMappings.HOME);

            return new ResponseEntity(
                    new JsonRespone("success", data),
                    HttpStatus.OK
            );

        } catch (ArticleAlreadyExists ex) {
            flash.info("Article already exists");

            data.put("url", path + UrlMappings.ARTICLE_CREATE);

            return new ResponseEntity(
                    new JsonRespone("success", data),
                    HttpStatus.OK
            );
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(UrlMappings.ARTICLE_RELEASED_LIST)
    public String releasedArticlesList(
            @RequestParam(required = false) String page,
            Authentication authentication,
            Model model
    ) {
        int cleanPage = SecurityUtility.cleanPageParam(page);

        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

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
        int cleanPage = SecurityUtility.cleanPageParam(page);

        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        Page<Article> articles
                = articleService.findAllUnreleasedArticlesByUser(
                        user.getId(),
                        PageRequest.of(cleanPage, perPage, Sort.by("id").descending())
                );

        model.addAttribute("articles", articles);

        return ViewNames.ARTICLE_UNRELEASED_LIST;
    }

    @GetMapping(UrlMappings.ARTICLE_SHOW + "/{id}")
    public String show(
            @PathVariable("id") String id,
            Model model,
            HttpServletRequest request
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Article article = articleService.findById(cleanId);

        if (!articleService.isItExists(article)) {
            flash.error("Unfortunately, Article not found");
            return UrlMappings.REDIRECT_HOME;
        }

        if (!articleService.isItReleased(article)) {
            flash.info("Unfortunately, Article not released");
            return UrlMappings.REDIRECT_HOME;
        }

        articleService.addView(article);

        String backUrl = (request.getHeader("Referer") != null)
                ? request.getHeader("Referer")
                : UrlMappings.HOME;

        if (!model.containsAttribute(AttributeNames.NEW_COMMENT)) {
            model.addAttribute(AttributeNames.NEW_COMMENT, new CommentForm());
        }
        model.addAttribute(AttributeNames.BACK_URL, backUrl);
        model.addAttribute("uriArticle", this.articleService.getImageUri(article));
        model.addAttribute(AttributeNames.ARTICLE, article);
        model.addAttribute("comments", this.commentService.findByArticleId(article.getId()));

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
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Article article = articleService.findById(cleanId);

        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        if (!articleService.isItExists(article)) {
            flash.error("Unfortunately, Article not found");
            return UrlMappings.REDIRECT_ARTICLE_UNRELEASED_LIST;
        }

        if (articleService.isItReleased(article)) {
            flash.info("Unfortunately, Article not released");
            return UrlMappings.REDIRECT_ARTICLE_UNRELEASED_LIST;
        }

        if (!Objects.equals(article.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("access forbidden");
        }

        if (!model.containsAttribute(AttributeNames.EDIT_ARTICLE)) {
            model.addAttribute(AttributeNames.EDIT_ARTICLE, new ArticleForm(
                    article.getTitle(),
                    article.getDescription(),
                    article.getBody(),
                    article.getCategory().getId()
            ));
        }
        model.addAttribute("id", cleanId);

        return ViewNames.EDIT_ARTICLE_FORM;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(UrlMappings.ARTICLE_UPDATE + "/{id}")
    @ResponseBody
    public ResponseEntity<?> update(
            @PathVariable("id") String id,
            @Valid @ModelAttribute(AttributeNames.EDIT_ARTICLE) ArticleForm formData,
            BindingResult result,
            Authentication authentication,
            HttpServletRequest request
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        String path = request.getContextPath();

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap();
            for (FieldError fe : result.getFieldErrors()) {
                if (!errors.containsKey(fe.getField())) {
                    errors.put(fe.getField(), fe.getDefaultMessage());
                }
            }

            return new ResponseEntity(
                    new JsonRespone("failed", errors),
                    HttpStatus.OK
            );
        }

        Article oldArticle = articleService.findById(cleanId);
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        if (articleService.isItReleased(oldArticle)) {
            return new ResponseEntity(
                    "bad request",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!Objects.equals(oldArticle.getUser().getId(), user.getId())) {
            return new ResponseEntity(
                    "access forbidden",
                    HttpStatus.FORBIDDEN
            );
        }

        Article article
                = new Article(formData.getTitle(), formData.getDescription(), formData.getBody());
        article.setCategory(categoryService.findById(formData.getCategoryId()));

        Map<String, Object> data = new HashMap();
        try {
            article = articleService.update(oldArticle, article);

            if (articleService.isItExists(article)) {
                flash.success("The article was updated");

            } else {
                flash.error("Unfortunately, there was a problem, "
                        + "please try again later");
            }

            MultipartFile image = formData.getImage();

            try {
                byte[] bytes = image.getBytes();
                String name = article.getId() + ".jpg";
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(new File("src/main/resources/static/img/article/" + name)));
                stream.write(bytes);
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            data.put("url", path + UrlMappings.ARTICLE_UNRELEASED_LIST);

            return new ResponseEntity(
                    new JsonRespone("success", data),
                    HttpStatus.OK
            );
        } catch (ArticleNotFound ex) {
            flash.info("Article not found");

            data.put("url", path + UrlMappings.ARTICLE_EDIT + "/" + cleanId);

            return new ResponseEntity(
                    new JsonRespone("success", data),
                    HttpStatus.OK
            );
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(UrlMappings.ARTICLE_DELETE + "/{id}")
    public String delete(
            @PathVariable("id") String id,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Article article = articleService.findById(cleanId);

        if (articleService.isItExists(article)) {
            User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

            if (!Objects.equals(article.getUser().getId(), user.getId())) {
                throw new AccessDeniedException("access forbidden");
            }

            articleService.delete(article);

            flash.success("The article was deleted");
        } else {
            flash.error("Unfortunately, there was a problem, "
                    + "please try again later");
        }

        return UrlMappings.REDIRECT_ARTICLE_UNRELEASED_LIST;
    }

    @GetMapping(UrlMappings.ARTICLE_CATEGORY_LIST + "/{id}")
    public String category(
            @PathVariable("id") String id,
            @RequestParam(required = false) String page,
            Model model
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);
        int cleanPage = SecurityUtility.cleanPageParam(page);

        Page<Article> articles
                = articleService.findByCategoryId(
                        cleanId,
                        PageRequest.of(cleanPage, perPage, Sort.by("publishedAt").descending())
                );

        Category category = categoryService.findById(cleanId);

        model.addAttribute("articles", articles);
        model.addAttribute("category", category);

        return ViewNames.ARTICLE_CATEGORY_LIST;
    }

    @GetMapping(UrlMappings.ARTICLE_USER_LIST + "/{id}")
    public String articleByUser(
            @PathVariable("id") String id,
            @RequestParam(required = false) String page,
            Model model
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);
        int cleanPage = SecurityUtility.cleanPageParam(page);

        User user = userService.findById(cleanId);

        Page<Article> articles
                = articleService.findByUserId(
                        user.getId(),
                        PageRequest.of(cleanPage, perPage, Sort.by("publishedAt").descending())
                );

        model.addAttribute("articles", articles);
        model.addAttribute("user", user);

        return ViewNames.ARTICLE_USER_LIST;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(UrlMappings.ARTICLE_LIKE + "/{id}")
    @ResponseBody
    public ResponseEntity<?> like(
            @PathVariable("id") String id,
            Authentication authentication,
            HttpServletRequest request
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Article article = articleService.findById(cleanId);

        if (articleService.isItExists(article)) {

            User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
            if (likeArticleService.isLikeUnique(user.getId(), article.getId())) {
                LikeArticle like = new LikeArticle();
                like.setArticle(article);
                like.setUser(user);
                article.setLikesCount(article.getLikesCount() + 1);
                likeArticleService.save(like);
                article = articleService.save(article);
            } else {
                LikeArticle like
                        = likeArticleService.findLike(user.getId(), article.getId());
                likeArticleService.deleteById(like.getId());
                article.setLikesCount(article.getLikesCount() - 1);
                article = articleService.save(article);
            }

            return new ResponseEntity(
                    new JsonRespone("success", article),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity(
                    "bad request",
                    HttpStatus.BAD_REQUEST
            );
        }

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(UrlMappings.ARTICLE_DISLIKE + "/{id}")
    @ResponseBody
    public ResponseEntity<?> dislike(
            @PathVariable("id") String id,
            Authentication authentication,
            HttpServletRequest request
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Article article = articleService.findById(cleanId);

        if (articleService.isItExists(article)) {

            User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
            if (dislikeArticleService.isDislikeUnique(user.getId(), article.getId())) {
                DislikeArticle dislike = new DislikeArticle();
                dislike.setArticle(article);
                dislike.setUser(user);
                dislikeArticleService.save(dislike);
                article.setDislikesCount(article.getDislikesCount() + 1);
                article = articleService.save(article);
            } else {
                DislikeArticle dislike
                        = dislikeArticleService.findDislike(user.getId(), article.getId());
                dislikeArticleService.deleteById(dislike.getId());
                article.setDislikesCount(article.getDislikesCount() - 1);
                article = articleService.save(article);
            }

            return new ResponseEntity(
                    new JsonRespone("success", article),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity(
                    "bad request",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
