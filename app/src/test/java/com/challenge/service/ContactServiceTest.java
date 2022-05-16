package com.challenge.service;

import com.challenge.domain.Contact;
import com.challenge.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ContactServiceTest {

    private static final String EMAIL_VALUE = "email";
    private static final String FIRSTNAME_VALUE = "firstname";
    private static final String LASTNAME_VALUE = "lastname";
    private static final Long ID_VALUE = 1L;

    @InjectMocks
    private ContactService contactService;

    @Mock
    private ContactRepository contactRepository;

    @Test
    void testCreateContact() {
        Contact contact = Contact.builder()
                .email(EMAIL_VALUE)
                .lastName(LASTNAME_VALUE)
                .firstName(FIRSTNAME_VALUE)
                .build();

        Contact contactExpected = Contact.builder()
                .contactId(ID_VALUE)
                .email(EMAIL_VALUE)
                .lastName(LASTNAME_VALUE)
                .firstName(FIRSTNAME_VALUE)
                .build();

        when(contactRepository.save(contact)).thenReturn(contactExpected);

        Contact result = contactService.createContact(contact);

        assertEquals(contactExpected, result);

        verify(contactRepository, times(1)).save(contact);
        verifyNoMoreInteractions(contactRepository);
    }

    @Test
    void testGetById() {
        Contact contactExpected = Contact.builder()
                .contactId(ID_VALUE)
                .email(EMAIL_VALUE)
                .lastName(LASTNAME_VALUE)
                .firstName(FIRSTNAME_VALUE)
                .build();

        when(contactRepository.findById(ID_VALUE)).thenReturn(Optional.ofNullable(contactExpected));

        Contact result = contactService.getById(ID_VALUE);

        assertEquals(contactExpected, result);

        verify(contactRepository, times(1)).findById(ID_VALUE);
        verifyNoMoreInteractions(contactRepository);
    }

    @Test
    void testGetByIdNotFoundException() {
        when(contactRepository.findById(ID_VALUE)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contactService.getById(ID_VALUE));

        verify(contactRepository, times(1)).findById(ID_VALUE);
        verifyNoMoreInteractions(contactRepository);
    }
}
