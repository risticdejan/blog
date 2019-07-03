package com.dejanristic.blog.repository;

import com.dejanristic.blog.domain.DislikeArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DislikeArticleRepository extends JpaRepository<DislikeArticle, Long> {

    DislikeArticle findByArticleIdAndUserId(Long articleId, Long userId);
}
