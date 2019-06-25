package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    Page<Article> findAll(Pageable pageable);

    Article findByTitle(String title);

    Article findById(Long id);

    Article createArticle(Article article);

    Article save(Article article);

    Article update(Long id, Article article);

}
