package com.challenge.service;

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

    @Transactional
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public Page<Message> findByContact(Long contactId, Pageable pageable) {
        return messageRepository.findAllByContact_ContactId(contactId, pageable);
    }
}
