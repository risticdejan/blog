package com.dejanristic.blog.domain;

import com.dejanristic.blog.validation.EmailVerification;
import com.dejanristic.blog.validation.FieldsVerification;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Entity
@FieldsVerification.List({
    @FieldsVerification(
            field = "password",
            fieldMatch = "confirmPassword",
            message = "Passwords do not match"
    )
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 45)
    @Pattern(regexp = "^[A-Za-z0-9._\\-]+$", message = "Username: can contain characters, digits, underscore and/or dash")
    @Column(name = "username", unique = true, nullable = false, length = 45)
    private String username;

    @NotBlank
    @Size(min = 6, max = 255)
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Transient
    private String confirmPassword;

    @NotBlank
    @Size(min = 4, max = 255)
    @EmailVerification
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", length = 19)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", length = 19)
    private Date updatedAt;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(Long id, String username, String password, String confirmPassword, String email, Date createdAt, Date updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
