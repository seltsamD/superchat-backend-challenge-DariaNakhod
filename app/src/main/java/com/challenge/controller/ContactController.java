package com.challenge.controller;

import com.challenge.domain.Contact;
import com.challenge.domain.Message;
import com.challenge.model.ContactCreateModel;
import com.challenge.model.ContactReadModel;
import com.challenge.model.InternalMessageCreateModel;
import com.challenge.model.MessageReadModel;
import com.challenge.service.ContactService;
import com.challenge.service.MessageService;
import com.challenge.service.replacer.PlaceholderReplacer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@Tag(name = "Contact API")
@ApiResponse(responseCode = "405", description = "Method Not Allowed")
@ApiResponse(responseCode = "406", description = "Not Acceptable")
@ApiResponse(responseCode = "500", description = "Internal Server Error ")
@Validated
@RequestMapping(value = "/api/v1/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ContactController {

    private final ConversionService conversionService;
    private final ContactService contactService;
    private final MessageService messageService;
    private final List<PlaceholderReplacer> placeholderReplacerList;

    @Operation(summary = "Create Contact", responses = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactReadModel.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "415", description = "Unsupported media type")})
    @ResponseStatus(CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ContactReadModel create(
            @Parameter(description = "Contact create request", required = true)
            @Valid @RequestBody ContactCreateModel request) {
        log.debug("Accepted request to create Contact {}", request);
        Contact contactToCreate = conversionService.convert(request, Contact.class);

        Contact contact = contactService.createContact(contactToCreate);
        log.debug("Contact successfully created {}", contact);

        return conversionService.convert(contact, ContactReadModel.class);
    }

    @Operation(summary = "Get the list of Contacts", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Page.class))}),
    })
    @GetMapping
    @PageableAsQueryParam
    Page<ContactReadModel> findAll(
            @SortDefault(sort = "contactId")
            @Parameter(hidden = true) Pageable pageable) {
        return contactService.findAll(pageable)
                .map(c -> conversionService.convert(c, ContactReadModel.class));
    }

    @Operation(summary = "Send message to contact", responses = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactReadModel.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "415", description = "Unsupported media type")},
            description = "To insert contact name use special word \\_name\\_ \n " +
                    "To insert current bitcoin price use special word \\_price\\_ ")
    @ResponseStatus(CREATED)
    @PostMapping(path = "/{contactId}/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
    MessageReadModel sendMessageToContact(
            @PathVariable("contactId") Long contactId,
            @Parameter(description = "Send message request", required = true)
            @Valid @RequestBody InternalMessageCreateModel request) {
        log.debug("Accepted request to send Message {}", request);

        Contact contact = contactService.getById(contactId);

        Message messageToCreate = Message.builder()
                .body(request.getBody())
                .contact(contact)
                .dateSend(LocalDateTime.now())
                .build();

        for (PlaceholderReplacer replacer: placeholderReplacerList) {
            if(replacer.canHandle(messageToCreate.getBody())) {
                String bodyWithReplacedValue = replacer.replace(messageToCreate);
                messageToCreate.setBody(bodyWithReplacedValue);
            }
        }

        Message message = messageService.createMessage(messageToCreate);
        log.debug("Message successfully created {}", message);

        return conversionService.convert(message, MessageReadModel.class);
    }

    @Operation(summary = "Get the list of Messages by contact", responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))}),
    })
    @GetMapping("/{contactId}/messages")
    @PageableAsQueryParam
    Page<MessageReadModel> getPreviousConversations(
            @PathVariable("contactId") Long contactId,
            @SortDefault(sort = "messageId") @Parameter(hidden = true) Pageable pageable) {
        return messageService.findByContact(contactId, pageable)
                .map(m -> conversionService.convert(m, MessageReadModel.class));
    }
}
