package com.dejanristic.blog.repository;

import com.dejanristic.blog.domain.LikeArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Long> {

    LikeArticle findByArticleIdAndUserId(Long articleId, Long userId);
}
