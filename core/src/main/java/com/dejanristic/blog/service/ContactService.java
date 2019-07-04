package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {

    Page<Contact> findAll(Pageable page);

    Contact findById(Long id);

    Contact create(Contact contact);

    void delete(Contact contact);

    void view(Contact contact);
}
