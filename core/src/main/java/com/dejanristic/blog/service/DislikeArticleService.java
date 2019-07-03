package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.DislikeArticle;
import java.util.List;

public interface DislikeArticleService {

    List<DislikeArticle> getAllDislike();

    DislikeArticle save(DislikeArticle dislike);

    DislikeArticle findById(Long id);

    DislikeArticle findDislike(Long userId, Long postId);

    void deleteById(Long id);

    boolean isDislikeUnique(Long userId, Long postId);
}
