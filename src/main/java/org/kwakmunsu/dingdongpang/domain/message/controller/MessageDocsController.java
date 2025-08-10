package org.kwakmunsu.dingdongpang.domain.message.controller;

import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.dingdongpang.domain.message.controller.dto.MessagesSuggestionRequest;
import org.kwakmunsu.dingdongpang.domain.message.service.dto.MessagesSuggestionResponse;
import org.kwakmunsu.dingdongpang.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Message API", description = "Message 관련 API 문서입니다.")
public abstract class MessageDocsController {

    @Operation(
            summary = "샘플 알림 메세지 생성 요청 API  - JWT O",
            description = """
                    ## 홍보용 샘플 알림 메세지를 요청하는 API 입니다.
                    - **공지 사항**
                      - 총 4개의 샘플 메세지가 반환됩니다.
                    """
    )
    @RequestBody(
            description = "샘플 알림 메세지 요청",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MessagesSuggestionRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공 ",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MessagesSuggestionResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MessagesSuggestionResponse> suggestionMessage(MessagesSuggestionRequest request);

}