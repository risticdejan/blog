package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.Article;

public interface ArticleService {

    Article findByTitle(String title);

    Article findById(Long id);

    Article createArticle(Article article);

    Article save(Article article);
}
