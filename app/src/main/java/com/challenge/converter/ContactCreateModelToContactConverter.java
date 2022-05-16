package com.challenge.converter;

import com.challenge.domain.Contact;
import com.challenge.model.ContactCreateModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ContactCreateModelToContactConverter implements Converter<ContactCreateModel, Contact> {

    @Override
    public Contact convert(ContactCreateModel source) {
        return Contact.builder()
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .email(source.getEmail())
                .phone(source.getPhone())
                .build();
    }
}
