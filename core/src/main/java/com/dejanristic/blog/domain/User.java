package com.dejanristic.blog.domain;

import com.dejanristic.blog.domain.security.Authority;
import com.dejanristic.blog.domain.security.Role;
import com.dejanristic.blog.domain.validation.FormValidationGroup;
import com.dejanristic.blog.domain.validation.PersistenceValidationGroup;
import com.dejanristic.blog.domain.validation.rules.EmailVerification;
import com.dejanristic.blog.domain.validation.rules.FieldsVerification;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@ToString
@Entity
@FieldsVerification.List({
    @FieldsVerification(
            field = "password",
            fieldMatch = "confirmPassword",
            message = "Passwords do not match",
            groups = {FormValidationGroup.class}
    )
})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @NotBlank(groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Size(min = 4, max = 45, groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Pattern(
            regexp = "^[A-Za-z0-9._\\-]+$",
            message = "Username: can contain characters, digits, underscore and/or dash",
            groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Column(name = "username", unique = true, nullable = false, length = 45)
    private String username;

    @NotBlank(groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Size(min = 6, max = 25, groups = {FormValidationGroup.class})
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Transient
    private String confirmPassword;

    @NotBlank(groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Size(min = 4, max = 255, groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @EmailVerification(groups = {PersistenceValidationGroup.class, FormValidationGroup.class})
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", length = 19)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", length = 19)
    private Date updatedAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
        CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = {
        @JoinColumn(name = "user_id")}, inverseJoinColumns = {
        @JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<Role>();

    private boolean enabled = true;

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

    public void addRole(Role role) {
        if (role == null) {
            return;
        }
        roles.add(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorites = new HashSet<>();
        roles.forEach(r -> authorites.add(new Authority(r.getName())));

        return authorites;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
