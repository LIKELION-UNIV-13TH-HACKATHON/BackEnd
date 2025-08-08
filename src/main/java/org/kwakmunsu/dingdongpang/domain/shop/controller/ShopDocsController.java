package org.kwakmunsu.dingdongpang.domain.shop.controller;

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
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopResponse;
import org.kwakmunsu.dingdongpang.global.annotation.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Shop API", description = "매장 관련 API 문서입니다.")
public abstract class ShopDocsController {

    @Operation(
            summary = "매장 정보 조회 API  - JWT O",
            description = """
                    ## 매장 정보 조회를 합니다.
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
            description = "매장 정보 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ShopResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<ShopResponse> getShop(Long shopId, Long memberId);

}