package com.dejanristic.blog.repository;

import com.dejanristic.blog.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {

    Article findByTitle(String title);

    @Query(value = "SELECT a FROM Article a WHERE a.publishedAt IS NOT NULL")
    Page<Article> findAllReleasedArticles(Pageable pageable);

    @Query(value = "SELECT a FROM Article a WHERE a.publishedAt IS NULL")
    Page<Article> findAllUnreleasedArticles(Pageable pageable);

    @Query(value = "SELECT a FROM Article a WHERE a.publishedAt IS NOT NULL AND a.user.id = :id")
    Page<Article> findAllReleasedArticlesByUser(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT a FROM Article a WHERE a.publishedAt IS NULL AND a.user.id = :id")
    Page<Article> findAllUnreleasedArticlesByUser(@Param("id") Long id, Pageable pageable);
}
