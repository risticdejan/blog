package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.DislikeArticle;
import com.dejanristic.blog.repository.DislikeArticleRepository;
import com.dejanristic.blog.service.DislikeArticleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DislikeArticleServiceImpl implements DislikeArticleService {

    private DislikeArticleRepository dislikeArticleRepository;

    @Autowired
    public DislikeArticleServiceImpl(DislikeArticleRepository dislikeArticleRepository) {
        this.dislikeArticleRepository = dislikeArticleRepository;
    }

    @Override
    public List<DislikeArticle> getAllDislike() {
        return this.dislikeArticleRepository.findAll();
    }

    @Override
    public DislikeArticle save(DislikeArticle dislike) {
        return this.dislikeArticleRepository.save(dislike);
    }

    @Override
    public DislikeArticle findById(Long id) {
        return this.dislikeArticleRepository.findById(id).orElse(null);
    }

    @Override
    public DislikeArticle findDislike(Long userId, Long postId) {
        return this.dislikeArticleRepository
                .findByArticleIdAndUserId(postId, userId);
    }

    @Override
    public void deleteById(Long id) {
        dislikeArticleRepository.deleteById(id);
    }

    @Override
    public boolean isDislikeUnique(Long userId, Long postId) {
        DislikeArticle dislike = this.findDislike(userId, postId);

        return (dislike == null);
    }
}
