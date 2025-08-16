package org.kwakmunsu.dingdongpang.domain.notification.controller;

import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.kwakmunsu.dingdongpang.domain.notification.controller.dto.NotifyCreateRequest;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyListResponse;
import org.kwakmunsu.dingdongpang.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = " Notification API", description = "알림 관련 API 문서입니다.")
public abstract class NotificationDocsController {

    @Operation(
            summary = "매장 알림 등록 요청 API  - JWT O",
            description = """
                    ## 매장 알림 등록을 합니다. 매장 관리자만 등록 가능합니다.
                    **안내 사항**
                     - NotifyCreateRequest, 알림 이미지는 FormData로 보내주세요.
                    """
    )
    @Parameter(
            name = "shopId",
            description = "매장 id ",
            in = ParameterIn.PATH,
            required = true
    )
    @ApiResponse(
            responseCode = "200",
            description = "매장 알림 등록 성공"
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> create(
            @Parameter(
                    description = "알림 등록 DTO ",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = NotifyCreateRequest.class))
            )
            NotifyCreateRequest request,
            @Parameter(
                    description = "알림 등록 이미지",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
                    )
            )
            List<MultipartFile> files,
            Long shopId,
            Long memberId
    );

    @Operation(
            summary = "사용자 알림 목록 조회 요청 API  - JWT O"
    )
    @ApiResponse(
            responseCode = "200",
            description = "사용자 알림 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = NotifyListResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<NotifyListResponse> getNotifications(Long memberId);

    @Operation(
            summary = "매장 조회 시 알림 목록 조회 API  - JWT O"
    )
    @ApiResponse(
            responseCode = "200",
            description = "매장 조회 시 알림 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = NotifyListResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<NotifyListResponse> getNotificationsByShop(@PathVariable Long shopId);
}