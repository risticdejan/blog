package com.dejanristic.blog.domain.form;

import com.dejanristic.blog.domain.validation.FormValidationGroup;
import com.dejanristic.blog.domain.validation.rules.EmailVerification;
import com.dejanristic.blog.domain.validation.rules.FieldsVerification;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@FieldsVerification.List({
    @FieldsVerification(
            field = "password",
            fieldMatch = "confirmPassword",
            message = "Passwords do not match"
    )
})
public class UserForm {

    @NotBlank
    @Size(min = 4, max = 45)
    @Pattern(
            regexp = "^[A-Za-z0-9._\\-]+$",
            message = "Username can contain characters, digits, underscore and/or dash")
    private String username;

    @NotBlank
    @Size(min = 6, max = 25, groups = {FormValidationGroup.class})
    private String password;

    @Transient
    private String confirmPassword;

    @NotBlank
    @Size(min = 4, max = 255)
    @EmailVerification
    private String email;

    public UserForm() {
    }

    public UserForm(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
