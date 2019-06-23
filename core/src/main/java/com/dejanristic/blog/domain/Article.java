package com.dejanristic.blog.domain;

import com.dejanristic.blog.domain.validation.FormValidationGroup;
import com.dejanristic.blog.domain.validation.PersistenceValidationGroup;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Size(min = 4, max = 255, groups = {FormValidationGroup.class})
    @Column(name = "title", unique = true, nullable = false, length = 255)
    private String title;

    @NotBlank(groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Size(min = 4, max = 511, groups = {FormValidationGroup.class})
    @Pattern(
            regexp = "^[A-Za-z0-9._\\-'\"\\s!?]+$",
            message = "Description cannot contain special characters",
            groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Column(name = "description", nullable = false, length = 511)
    private String description;

    @NotBlank(groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Size(min = 4, max = 65535, groups = {FormValidationGroup.class})
    @Column(columnDefinition = "TEXT", name = "body", nullable = false)
    private String body;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "published_at", length = 19)
    private Date publishedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", length = 19)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", length = 19)
    private Date updatedAt;

    public Article() {
    }

    public Article(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

    public Article(
            Long id, String title, String description, String body,
            Date publishedAt, Date createdAt, Date updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.body = body;
        this.publishedAt = publishedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
