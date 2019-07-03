package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.Article;
import com.dejanristic.blog.exception.ArticleAlreadyExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    boolean isItExists(Article article);

    boolean isItReleased(Article article);

    Page<Article> findByCategoryId(Long id, Pageable pageable);

    Page<Article> findByUserId(Long id, Pageable pageable);

    Page<Article> findAllReleasedArticles(Pageable pageable);

    Page<Article> findAllUnreleasedArticles(Pageable pageable);

    Page<Article> findAllReleasedArticlesByUser(Long id, Pageable pageable);

    Page<Article> findAllUnreleasedArticlesByUser(Long id, Pageable pageable);

    Article findByTitle(String title);

    Article findById(Long id);

    Article create(Article article) throws ArticleAlreadyExists;

    Article update(Article odlArticle, Article article) throws ArticleAlreadyExists;

    void delete(Article article);

    void release(Article article);

    Article save(Article article);

    void addView(Article article);

}
