package com.dejanristic.blog.repository;

import com.dejanristic.blog.domain.Article;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {

    Article findByTitle(String title);
}
