package com.challenge.converter;

import com.challenge.domain.Contact;
import com.challenge.domain.Message;
import com.challenge.model.MessageCreateModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageCreateModelToMessageConverter implements Converter<MessageCreateModel, Message> {

    @Override
    public Message convert(MessageCreateModel source) {
        return Message.builder()
                .body(source.getBody())
                .contact(Contact.builder()
                        .email(source.getEmail())
                        .build())
                .dateSend(LocalDateTime.now())
                .build();
    }
}
