package com.challenge.controller;

import com.challenge.domain.Message;
import com.challenge.model.ContactReadModel;
import com.challenge.model.MessageCreateModel;
import com.challenge.model.MessageReadModel;
import com.challenge.service.MessageService;
import com.challenge.service.PlaceholderReplacer;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@Tag(name = "Message API")
@ApiResponse(responseCode = "405", description = "Method Not Allowed")
@ApiResponse(responseCode = "406", description = "Not Acceptable")
@ApiResponse(responseCode = "500", description = "Internal Server Error ")
@Validated
@RequestMapping(value = "/api/v1/messages", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j

public class MessageController {

    private final ConversionService conversionService;
    private final MessageService messageService;
    private final List<PlaceholderReplacer> placeholderReplacerList;

    @Operation(summary = "Send message", responses = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactReadModel.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "415", description = "Unsupported media type")},
            description = "To insert contact name use special word _name_")
    @ResponseStatus(CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    MessageReadModel create(
            @Parameter(description = "Send message request", required = true)
            @Valid @RequestBody MessageCreateModel request) {
        log.debug("Accepted request to send Message {}", request);
        Message messageToCreate = conversionService.convert(request, Message.class);

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

    @Operation(summary = "Get the list of Contacts", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Page.class))}),
    })
    @GetMapping
    @PageableAsQueryParam
    Page<MessageReadModel> findAll(
            @SortDefault(sort = "messageId")
            @Parameter(hidden = true) Pageable pageable) {
        return messageService.findAll(pageable)
                .map(m -> conversionService.convert(m, MessageReadModel.class));
    }
}
