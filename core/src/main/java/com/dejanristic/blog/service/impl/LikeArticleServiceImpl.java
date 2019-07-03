package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.LikeArticle;
import com.dejanristic.blog.repository.LikeArticleRepository;
import com.dejanristic.blog.service.LikeArticleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeArticleServiceImpl implements LikeArticleService {

    private LikeArticleRepository likeArticleRepository;

    @Autowired
    public LikeArticleServiceImpl(LikeArticleRepository likeArticleRepository) {
        this.likeArticleRepository = likeArticleRepository;
    }

    @Override
    public List<LikeArticle> getAllLike() {
        return this.likeArticleRepository.findAll();
    }

    @Override
    public LikeArticle save(LikeArticle like) {
        return this.likeArticleRepository.save(like);
    }

    @Override
    public LikeArticle findById(Long id) {
        return this.likeArticleRepository.findById(id).orElse(null);
    }

    @Override
    public LikeArticle findLike(Long userId, Long postId) {
        return this.likeArticleRepository
                .findByArticleIdAndUserId(postId, userId);
    }

    @Override
    public void deleteById(Long id) {
        this.likeArticleRepository.deleteById(id);
    }

    @Override
    public boolean isLikeUnique(Long userId, Long postId) {
        LikeArticle like = this.findLike(userId, postId);

        return (like == null);
    }

}
