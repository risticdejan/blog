package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.repository.ArticleRepository;
import com.dejanristic.blog.service.ArticleService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
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
    public Article findByTitle(String title) {
        return this.articleRepository.findByTitle(title);
    }

    @Override
    public Article findById(Long id) {
        return this.articleRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Article createArticle(Article article) {
        Article oldArticle = articleRepository.findByTitle(article.getTitle());

        if (oldArticle != null) {
            log.info("article {} already exists", article.getTitle());
            return null;
        } else {
            article.setPublishedAt(Timestamp.valueOf(LocalDateTime.now()));
            article.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            article.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            return articleRepository.save(article);
        }
    }

    @Override
    public Article save(Article article) {
        return this.articleRepository.save(article);
    }

}
