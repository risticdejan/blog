package com.dejanristic.blog.domain.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ArticleForm {

    @NotNull
    private Long categoryId;

    @NotBlank
    @Size(min = 4, max = 155)
    @Pattern(
            regexp = "^[A-Za-z0-9.,_\\-'\"\\s!?]+$",
            message = "Description cannot contain special characters")
    private String title;

    @NotBlank
    @Size(min = 4, max = 255)
    @Pattern(
            regexp = "^[A-Za-z0-9.,_\\-\\'\"\\s!?]+$",
            message = "Description cannot contain special characters")
    private String description;

    @NotBlank
    @Size(min = 4, max = 65535)
    private String body;

    public ArticleForm() {
    }

    public ArticleForm(
            String title,
            String description,
            String body,
            Long categoryId
    ) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.categoryId = categoryId;
    }

}
