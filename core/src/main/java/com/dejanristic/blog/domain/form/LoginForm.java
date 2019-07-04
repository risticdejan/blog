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
public class LoginForm {

    @NotBlank
    @Size(min = 4, max = 45)
    @Pattern(
            regexp = "^[A-Za-z0-9._\\-]+$",
            message = "Username can contain characters, digits, underscore and/or dash")
    private String username;

    @NotBlank
    @Size(min = 6, max = 25)
    private String password;

    public LoginForm() {
    }

    public LoginForm(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
