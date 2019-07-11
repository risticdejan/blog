package com.dejanristic.blog.repository;

import com.dejanristic.blog.domain.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleId(Long articleId);

    List<Comment> findByArticleIdOrderByCreatedAtDesc(Long articleId);

    Page<Comment> findByUserId(Long id, Pageable pageable);
}
