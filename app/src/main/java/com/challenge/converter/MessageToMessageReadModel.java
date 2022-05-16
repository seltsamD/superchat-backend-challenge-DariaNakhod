package com.challenge.converter;

import com.challenge.domain.Message;
import com.challenge.model.MessageReadModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MessageToMessageReadModel implements Converter<Message, MessageReadModel> {

    @Override
    public MessageReadModel convert(Message source) {
        return MessageReadModel.builder()
                .messageId(source.getMessageId())
                .body(source.getBody())
                .dateSend(source.getDateSend())
                .email(source.getContact().getEmail())
                .build();
    }
}
