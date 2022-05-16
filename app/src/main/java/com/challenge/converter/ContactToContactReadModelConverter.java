package com.challenge.converter;

import com.challenge.domain.Contact;
import com.challenge.model.ContactReadModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ContactToContactReadModelConverter implements Converter<Contact, ContactReadModel> {

    @Override
    public ContactReadModel convert(Contact source) {
        return ContactReadModel.builder()
                .contactId(source.getContactId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .email(source.getEmail())
                .phone(source.getPhone())
                .build();
    }
}
