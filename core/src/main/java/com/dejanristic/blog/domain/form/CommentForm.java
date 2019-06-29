package com.dejanristic.blog.domain.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentForm {

    @NotBlank
    @Size(min = 4, max = 512)
    @Pattern(
            regexp = "^[A-Za-z0-9.,_\\-\\'\"\\s!?]+$",
            message = "Comment cannot contain special characters")
    private String body;
}
