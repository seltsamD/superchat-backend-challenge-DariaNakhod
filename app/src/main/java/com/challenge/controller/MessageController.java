package com.challenge.controller;

import com.challenge.domain.Contact;
import com.challenge.domain.Message;
import com.challenge.model.ContactReadModel;
import com.challenge.model.MessageCreateModel;
import com.challenge.model.MessageReadModel;
import com.challenge.service.ContactService;
import com.challenge.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@Tag(name = "Message API")
@ApiResponse(responseCode = "405", description = "Method Not Allowed")
@ApiResponse(responseCode = "406", description = "Not Acceptable")
@ApiResponse(responseCode = "500", description = "Internal Server Error")
@Validated
@RequestMapping(value = "/api/v1/messages", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j

public class MessageController {

    private final ConversionService conversionService;
    private final MessageService messageService;
    private final ContactService contactService;

    @Operation(summary = "Send message", responses = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactReadModel.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "415", description = "Unsupported media type")},
            description = "To insert contact name use special word _name_")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    MessageReadModel sendMessageHook(
            @Parameter(description = "Send message request", required = true)
            @Valid @RequestBody MessageCreateModel request) {
        log.debug("Accepted request to send Message {}", request);
        Message messageToCreate = conversionService.convert(request, Message.class);

        Contact contact = contactService.getByEmail(request.getEmail());
        messageToCreate.setContact(contact);

        Message message = messageService.createMessage(messageToCreate);
        log.debug("Message successfully created {}", message);
        return conversionService.convert(message, MessageReadModel.class);
    }

}
