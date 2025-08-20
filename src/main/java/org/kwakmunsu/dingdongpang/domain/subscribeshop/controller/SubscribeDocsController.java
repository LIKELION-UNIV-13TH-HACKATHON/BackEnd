package org.kwakmunsu.dingdongpang.domain.subscribeshop.controller;

import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.ALREADY_SUBSCRIBE_SHOP;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.FORBIDDEN_ERROR;
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
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopListResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.service.dto.DailySubscriptionListResponse;
import org.kwakmunsu.dingdongpang.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "ShopSubscribe API", description = "매장 구독 관련 API 문서입니다.")
public abstract class SubscribeDocsController {

    @Operation(
            summary = "매장 구독 요청 API  - JWT O",
            description = """
                    ## 매장 구독을 합니다.
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
            description = "매장 구독 성공"
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            ALREADY_SUBSCRIBE_SHOP,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> subscribe(Long shopId, Long memberId);

    @Operation(
            summary = "매장 구독 취소 요청 API  - JWT O",
            description = """
                    ## 매장 구독을 취소 합니다.
                    """
    )
    @Parameter(
            name = "shopId",
            description = "매장 id ",
            in = ParameterIn.PATH,
            required = true
    )
    @ApiResponse(
            responseCode = "204",
            description = "매장 구독 취소 성공"
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> unsubscribe(Long shopId, Long memberId);


    @Operation(summary = "구독 매장 목록 조회 - JWT O ")
    @ApiResponse(
            responseCode = "200",
            description = "구독 매장 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SubscribeShopListResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<SubscribeShopListResponse> getSubscribedShop(Long memberId);

    @Operation(
            summary = "최근 일주일 간 일별 신규 매장 구독자 수 조회 - JWT O ",
            description = """
                    ### 최근 일주일 간 일별 신규 매장 구독자 수 API 안내
                    - 오늘 날짜 포함 일주일 간 데이터를 조회힙니다.
                    - 오래된 순입니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "최근 일주일 간 구독자 수 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DailySubscriptionListResponse.class)
            )
    )
    @Parameter(
            name = "shopId",
            description = "매장 id ",
            in = ParameterIn.PATH,
            required = true
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<DailySubscriptionListResponse> getWeeklySubscriptions(Long shopId, Long memberId);
}