package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.Contact;
import com.dejanristic.blog.repository.ContactRepository;
import com.dejanristic.blog.service.ContactService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService {

    private ContactRepository contactRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Page<Contact> findAll(Pageable page) {
        return this.contactRepository.findAll(page);
    }

    @Override
    public Contact findById(Long id) {
        return this.contactRepository.findById(id).orElse(null);
    }

    @Override
    public Contact create(Contact contact) {
        contact.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        contact.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return this.contactRepository.save(contact);
    }

    @Override
    public void delete(Contact contact) {
        this.contactRepository.delete(contact);
    }

    @Override
    public void view(Contact contact) {
        contact.setView(true);

        this.contactRepository.save(contact);
    }

}
