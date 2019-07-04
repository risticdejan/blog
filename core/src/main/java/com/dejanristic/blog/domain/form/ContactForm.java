package com.dejanristic.blog.domain.form;

import com.dejanristic.blog.domain.validation.rules.EmailVerification;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContactForm {

    @NotBlank
    @Size(min = 4, max = 45)
    @Pattern(
            regexp = "^[A-Za-z0-9._\\-]+$",
            message = "Name can contain characters, digits, underscore and/or dash")
    private String name;

    @NotBlank
    @Size(min = 4, max = 512)
    @Pattern(
            regexp = "^[A-Za-z0-9.,_\\-\\'\"\\s!?]+$",
            message = "Message cannot contain special characters")
    private String body;

    @NotBlank
    @Size(min = 4, max = 255)
    @EmailVerification
    private String email;

    @NotBlank
    @Size(min = 4, max = 32)
    @Pattern(
            regexp = "^[0-9._\\-\\s]+$",
            message = "Pone can contain numbers")
    private String phone;

}
