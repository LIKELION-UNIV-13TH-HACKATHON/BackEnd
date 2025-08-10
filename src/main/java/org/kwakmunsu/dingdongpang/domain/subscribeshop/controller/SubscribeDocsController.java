package org.kwakmunsu.dingdongpang.domain.subscribeshop.controller;

import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.ALREADY_SUBSCRIBE_SHOP;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.dingdongpang.global.swagger.ApiExceptions;
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

}