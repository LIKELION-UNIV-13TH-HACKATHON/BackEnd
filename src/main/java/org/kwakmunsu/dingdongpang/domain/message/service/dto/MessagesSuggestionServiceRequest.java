package org.kwakmunsu.dingdongpang.domain.message.service.dto;

import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.message.service.MessageType;

@Builder
public record MessagesSuggestionServiceRequest(String message, MessageType messageType) {

}