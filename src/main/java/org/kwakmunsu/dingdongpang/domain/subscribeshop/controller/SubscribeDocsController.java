package org.kwakmunsu.dingdongpang.domain.subscribeshop.controller;

import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.ALREADY_SUBSCRIBE_SHOP;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopListResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopListResponse;
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


    @Operation(
            summary = "구독 매장 목록 조회 - JWT O ",
            description = """
                    ### 매장 목록 조회 API 안내
                    - **위도(latitude) 경도(longitude) 는 필수값입니다!**
                    - **페이징 처리는 20개씩 진행됩니다**.
                    - **커서 페이징**
                      - `lastShopId` 파라미터로 Cursor 기반 페이징 처리가 가능합니다.
                      - Null 값은 처음부터 조회하며, 이후에는 마지막으로 조회된 매장 ID를 입력하여 다음 페이지를 조회합니다.
                    - 예시: `?lastShopId=10&longitude=124.2133&latitude=54.2123` <br>
                    - 파라미터의 정확한 값을 입력해주세요.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "구독 매장 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ShopListResponse.class)
            )
    )
    @Parameters(value = {
            @Parameter(
                    name = "lastShopId",
                    description = "마지막으로 조회된 매장 ID (페이지네이션, Cursor 기반)",
                    schema = @Schema(type = "Long", example = "10")
            ),
            @Parameter(
                    name = "longitude",
                    required = true,
                    description = "현재 사용자 위치 - 경도",
                    schema = @Schema(type = "Double", example = "127.213123")
            ),
            @Parameter(
                    name = "latitude",
                    required = true,
                    description = "현재 사용자 위치 - 위도",
                    schema = @Schema(type = "Double", example = "22.2323")
            )
    })
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<SubscribeShopListResponse> getSubscribedShop(
            Long memberId,
            Long lastShopId,
            Double longitude,
            Double latitude
    );

}