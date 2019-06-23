package com.dejanristic.blog.repository;

import com.dejanristic.blog.domain.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {

    Article findByTitle(String title);
}
