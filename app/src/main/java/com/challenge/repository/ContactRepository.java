package com.challenge.repository;

import com.challenge.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> getContactByEmailEquals(String email);
}
