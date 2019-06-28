package com.dejanristic.blog.domain;

import com.dejanristic.blog.domain.validation.FormValidationGroup;
import com.dejanristic.blog.domain.validation.PersistenceValidationGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NotBlank(groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Size(min = 3, max = 65, groups = {FormValidationGroup.class})
    @Pattern(
            regexp = "^[A-Za-z0-9.,_\\-'\"\\s!?]+$",
            message = "Name cannot contain special characters",
            groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Column(name = "name", unique = true, nullable = false, length = 65)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @JsonIgnore
    private Set<Article> articles = new HashSet<>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
