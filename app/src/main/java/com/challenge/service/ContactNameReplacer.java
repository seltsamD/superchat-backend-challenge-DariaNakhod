package com.challenge.service;

import com.challenge.domain.Contact;
import com.challenge.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactNameReplacer implements PlaceholderReplacer {

    private static final String PATTERN_TO_REPLACE = "_name_";
    private final ContactService contactService;

    @Override
    public boolean canHandle(String message) {
        return message.contains(PATTERN_TO_REPLACE);
    }

    @Override
    public String replace(Message message) {
        Contact contact = contactService.getByEmail(message.getContact().getEmail());
        String contactName = contact.getFirstName() + " " + contact.getLastName();

        String body = message.getBody();
        return body.replaceAll(PATTERN_TO_REPLACE, contactName);
    }
}
