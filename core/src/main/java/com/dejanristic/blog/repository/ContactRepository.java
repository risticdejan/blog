package com.dejanristic.blog.repository;

import com.dejanristic.blog.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}
