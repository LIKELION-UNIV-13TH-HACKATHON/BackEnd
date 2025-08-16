package org.kwakmunsu.dingdongpang.domain.shop.controller;

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
import java.util.List;
import org.kwakmunsu.dingdongpang.domain.shop.controller.dto.MerchantUpdateRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.SortBy;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopListResponse;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopDashboardResponse;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopNearbySearchListResponse;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopResponse;
import org.kwakmunsu.dingdongpang.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Shop API", description = "매장 관련 API 문서입니다.")
public abstract class ShopDocsController {

    @Operation(
            summary = "매장 목록 조회(정렬 검색 포함) - JWT O",
            description = """
                    ### 매장 목록 조회 API 안내
                    - **위도(latitude) 경도(longitude) 는 필수값입니다!**
                    - **페이징 처리는 20개씩 진행됩니다**.
                    - **검색**
                      - `q` 파라미터로 매장 이름을 기준으로 검색할 수 있습니다.
                    - **정렬**
                      - `sortBy` 파라미터로 정렬 방식을 설정할 수 있습니다.
                    - **커서 페이징**
                      - `lastShopId` 파라미터로 Cursor 기반 페이징 처리가 가능합니다.
                      - `lastSubscribeCount` 는 마지막 응답에서 추출하시면 됩니다.
                      - `lastDistance` 는 마지막 응답에서 추출하시면 됩니다.
                      - Null 값은 처음부터 조회하며, 이후에는 마지막으로 조회된 매장 ID를 입력하여 다음 페이지를 조회합니다.
                    - 예시: `?q=운동&sortBy=NEAREST&lastShopId=10&longitude=124.2133&latitude=54.2123` <br>
                    - 파라미터의 정확한 값을 입력해주세요.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "매장 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ShopListResponse.class)
            )
    )
    @Parameters(value = {
            @Parameter(
                    name = "q",
                    description = "검색 키워드 - 매장 이름 기준 검색",
                    example = "대훈이네",
                    schema = @Schema(type = "String")
            ),
            @Parameter(
                    name = "sortBy",
                    description = "NEWEST(최신순, 기본값) |  MOST_SUBSCRIBED(구독자순) | NEAREST(거리순)",
                    schema = @Schema(implementation = SortBy.class)
            ),
            @Parameter(
                    name = "lastShopId",
                    description = "마지막으로 조회된 매장 ID (페이지네이션, Cursor 기반)",
                    schema = @Schema(type = "Long", example = "10")
            ),
            @Parameter(
                    name = "lastSubscribeCount",
                    description = "구독 순 정렬 시 마지막으로 조회된 구독자 수 (페이지네이션, Cursor 기반)",
                    schema = @Schema(type = "Long", example = "10")
            ),
            @Parameter(
                    name = "lastDistance",
                    description = "거리 순 정렬 시 마지막으로 조회된 거리 (페이지네이션, Cursor 기반)",
                    schema = @Schema(type = "Double", example = "246.12345")
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
    public abstract ResponseEntity<ShopListResponse> getShopList(
            Long memberId,
            String q,
            SortBy sortBy,
            Long lastShopId,
            Long lastSubscribeCount,
            Double lastDistance,
            Double longitude,
            Double latitude
    );

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

    @Operation(
            summary = "주변 매장 조회 - JWT O",
            description = """
                    ### 주변 매장 조회 API 안내
                    - **위도(latitude) 경도(longitude)는 필수값입니다!**
                    - **반경 범위 내에 서비스에 등록된 매장들을 조회합니다**.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "주변 매장 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ShopNearbySearchListResponse.class)
            )
    )
    @Parameters(value = {
            @Parameter(
                    name = "radiusMeters",
                    required = true,
                    description = "반경 범위 - M 단위",
                    example = "500",
                    schema = @Schema(type = "Integer")
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
    public abstract ResponseEntity<ShopNearbySearchListResponse> getNearbyShops(
            Double longitude,
            Double latitude,
            int radiusMeters
    );

    @Operation(
            summary = "매장 정보 수정 요청 API  - JWT O",
            description = """
                    ## 매장 정보를 수정 합니다.
                    **안내 사항**
                     - MerchantUpdateRequest, 매장 대표 이미지, 매장 이미지는 FormData로 보내주세요.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "매장 정보 수정 성공"
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> updateShop(
            @Parameter(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MerchantUpdateRequest.class))
            )
            MerchantUpdateRequest request,
            @Parameter(
                    description = "매장 대표 이미지",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            MultipartFile mainImage,
            @Parameter(
                    description = "매장 이미지",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            List<MultipartFile> imageFiles,
            Long memberId
    );

    @Operation(
            summary = "매장 요약 대시보드 조회 API  - JWT O",
            description = """
                    ## 매장 요약 대시보드를 합니다.
                    - 오늘 알림 발송 수
                    - 매장 조회수
                    - 신규 구독자 수
                    - 총 구독자 수
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
            description = "매장 요약 대시보드 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ShopDashboardResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<ShopDashboardResponse> getDashboard(Long shopId);
}