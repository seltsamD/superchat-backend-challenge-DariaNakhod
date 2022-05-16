package com.challenge.service;

import com.challenge.domain.Contact;
import com.challenge.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Transactional
    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Transactional(readOnly = true)
    public Page<Contact> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Contact getByEmail(String email) {
        return contactRepository.getContactByEmailEquals(email);
    }
}
