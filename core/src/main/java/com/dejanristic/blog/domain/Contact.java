package com.dejanristic.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "body", nullable = false, length = 511)
    private String body;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "phone", nullable = false, length = 32)
    private String phone;

    @Column(name = "view")
    private boolean view = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", length = 19)
    @JsonIgnore
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", length = 19)
    @JsonIgnore
    private Date updatedAt;

    public Contact() {
    }

    public Contact(
            String name,
            String body,
            String email,
            String phone
    ) {
        this.name = name;
        this.body = body;
        this.email = email;
        this.phone = phone;
    }
}
