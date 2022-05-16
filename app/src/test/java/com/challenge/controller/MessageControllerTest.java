package com.challenge.controller;

import com.challenge.domain.Contact;
import com.challenge.domain.Message;
import com.challenge.model.MessageCreateModel;
import com.challenge.model.MessageReadModel;
import com.challenge.service.ContactService;
import com.challenge.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class MessageControllerTest {

    private static final String EMAIL_VALUE = "email";
    private static final String BODY_VALUE = "body";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @InjectMocks
    private MessageController controller;

    @Mock
    private ConversionService conversionService;

    @Mock
    private MessageService messageService;

    @Mock
    private ContactService contactService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new DefaultControllerAdvice())
                .build();
    }

    @Test
    void testSendMessage() throws Exception {
        MessageCreateModel messageCreateModel = MessageCreateModel.builder()
                .email(EMAIL_VALUE)
                .body(BODY_VALUE)
                .build();

        Contact contact = Contact.builder()
                .contactId(1L)
                .email(EMAIL_VALUE)
                .build();

        Message message = Message.builder()
                .body(BODY_VALUE)
                .contact(contact)
                .messageId(1L)
                .build();

        MessageReadModel messageReadModel = MessageReadModel.builder()
                .body(BODY_VALUE)
                .email(EMAIL_VALUE)
                .messageId(1L)
                .dateSend(LocalDateTime.now())
                .build();

        when(conversionService.convert(messageCreateModel, Message.class))
                .thenReturn(message);
        when(contactService.getByEmail(EMAIL_VALUE)).thenReturn(contact);
        when(messageService.createMessage(message)).thenReturn(message);
        when(conversionService.convert(message, MessageReadModel.class))
                .thenReturn(messageReadModel);

        mockMvc.perform(post("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageCreateModel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.body")
                        .value(message.getBody()))
                .andExpect(jsonPath("$.email")
                        .value(message.getContact().getEmail()))
                .andExpect(jsonPath("$.messageId")
                        .value(message.getMessageId()))
                .andExpect(jsonPath("$.dateSend")
                        .isNotEmpty());


        verify(conversionService, times(1)).convert(messageCreateModel, Message.class);
        verify(contactService, times(1)).getByEmail(EMAIL_VALUE);
        verify(messageService, times(1)).createMessage(message);
        verify(conversionService, times(1)).convert(message, MessageReadModel.class);
        verifyNoMoreInteractions(conversionService, contactService, messageService);
    }

}
