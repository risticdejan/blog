package com.dejanristic.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    @OrderBy(value = "createdAt DESC")
    @JsonIgnore
    private Set<Comment> comments;

    @Column(name = "views_count")
    private Integer viewsCount;

    @Column(name = "likes_count")
    private Integer likesCount;

    @Column(name = "dislike_count")
    private Integer dislikesCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    @JsonIgnore
    private Set<LikeArticle> likes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article")
    @JsonIgnore
    private Set<DislikeArticle> dislikes;

    @Column(name = "title", unique = true, nullable = false, length = 155)
    private String title;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(columnDefinition = "TEXT", name = "body", nullable = false)
    private String body;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "published_at", length = 19)
    private Date publishedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", length = 19)
    @JsonIgnore
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", length = 19)
    @JsonIgnore
    private Date updatedAt;

    public Article() {
    }

    public Article(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }
}
