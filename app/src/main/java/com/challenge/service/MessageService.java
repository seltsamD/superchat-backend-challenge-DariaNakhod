package com.challenge.service;

import com.challenge.domain.Contact;
import com.challenge.domain.Message;
import com.challenge.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ContactService contactService;

    @Transactional
    public Message createMessage(Message message) {
        Contact contact = contactService.getByEmail(message.getContact().getEmail());
        message.setContact(contact);
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public Page<Message> findAll(Pageable pageable) {
        return messageRepository.findAll(pageable);
    }
}
