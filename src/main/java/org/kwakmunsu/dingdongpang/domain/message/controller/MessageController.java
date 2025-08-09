package org.kwakmunsu.dingdongpang.domain.message.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.message.controller.dto.MessagesSuggestionRequest;
import org.kwakmunsu.dingdongpang.domain.message.service.MessageService;
import org.kwakmunsu.dingdongpang.domain.message.service.dto.MessagesSuggestionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MessageController extends MessageDocsController {

    private final MessageService messageService;

    @Override
    @PostMapping("/messages/suggestions")
    public ResponseEntity<MessagesSuggestionResponse> suggestionMessage(
          @Valid @RequestBody MessagesSuggestionRequest request
    ) {
        MessagesSuggestionResponse response = messageService.suggestionMessage(request.toServiceRequest());

        return ResponseEntity.ok(response);
    }

}