package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.LikeArticle;
import java.util.List;

public interface LikeArticleService {

    List<LikeArticle> getAllLike();

    LikeArticle save(LikeArticle like);

    LikeArticle findById(Long id);

    LikeArticle findLike(Long userId, Long postId);

    void deleteById(Long id);

    boolean isLikeUnique(Long userId, Long postId);
}
