package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.exception.ArticleAlreadyExists;
import com.dejanristic.blog.exception.ArticleNotFound;
import com.dejanristic.blog.repository.ArticleRepository;
import com.dejanristic.blog.service.ArticleService;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public boolean isItExists(Article article) {
        return article != null;
    }

    @Override
    public boolean isItReleased(Article article) {
        return article != null && article.getPublishedAt() != null;
    }

    @Override
    public Page<Article> findByCategoryId(Long id, Pageable pageable) {
        return this.articleRepository.findByCategoryId(id, pageable);
    }

    @Override
    public Page<Article> findByUserId(Long id, Pageable pageable) {
        return this.articleRepository.findByUserId(id, pageable);
    }

    @Override
    public Article findByTitle(String title) {
        return this.articleRepository.findByTitle(title);
    }

    @Override
    public Article findById(Long id) {
        return this.articleRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Article> findAllReleasedArticles(Pageable pageable) {
        return (Page<Article>) this.articleRepository.findAllReleasedArticles(pageable);
    }

    @Override
    public Page<Article> findAllUnreleasedArticles(Pageable pageable) {
        return (Page<Article>) this.articleRepository.findAllUnreleasedArticles(pageable);
    }

    @Override
    public Page<Article> findAllReleasedArticlesByUser(Long id, Pageable pageable) {
        return (Page<Article>) this.articleRepository.findAllReleasedArticlesByUser(id, pageable);
    }

    @Override
    public Page<Article> findAllUnreleasedArticlesByUser(Long id, Pageable pageable) {
        return (Page<Article>) this.articleRepository.findAllUnreleasedArticlesByUser(id, pageable);
    }

    @Override
    public Page<Article> findAllArticlesByUser(Long userId, Pageable pageable) {
        return (Page<Article>) this.articleRepository.findByUserId(userId, pageable);
    }

    @Override
    @Transactional
    public Article create(Article article) throws ArticleAlreadyExists {
        Article oldArticle = articleRepository.findByTitle(article.getTitle());

        if (oldArticle != null) {
            log.info("article {} already exists", article.getTitle());
            throw new ArticleAlreadyExists("article alredy exists");
        } else {
            article.setCommentsCount(0);
            article.setLikesCount(0);
            article.setDislikesCount(0);
            article.setViewsCount(0);
            article.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            article.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            return articleRepository.save(article);
        }
    }

    @Override
    @Transactional
    public Article update(Article oldArticle, Article article) throws ArticleNotFound {
        if (oldArticle == null) {
            throw new ArticleNotFound("article not found");
        } else {
            oldArticle.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            oldArticle.setTitle(article.getTitle());
            oldArticle.setDescription(article.getDescription());
            oldArticle.setBody(article.getBody());
            oldArticle.setCategory(article.getCategory());

            return this.articleRepository.save(oldArticle);
        }
    }

    @Override
    public void delete(Article article) {
        this.articleRepository.delete(article);
    }

    @Override
    public void release(Article article) {
        article.setPublishedAt(Timestamp.valueOf(LocalDateTime.now()));
        this.articleRepository.save(article);
    }

    @Override
    public Article save(Article article) {
        return this.articleRepository.save(article);
    }

    @Override
    public void addView(Article article) {
        article.setViewsCount(article.getViewsCount() + 1);
        this.articleRepository.save(article);
    }

    @Override
    public Long count() {
        return this.articleRepository.count();
    }

    @Override
    public String getImageUri(Article article) {
        File tempFile = new File("src/main/resources/static/img/article/" + article.getId() + ".jpg");

        if (tempFile.exists()) {
            return "/img/article/" + article.getId() + ".jpg";
        } else {
            return "/img/post-bg.jpg";
        }
    }

}
