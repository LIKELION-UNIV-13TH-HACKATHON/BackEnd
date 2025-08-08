package org.kwakmunsu.dingdongpang.domain.menu.controller;

import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.DUPLICATE;
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
import org.kwakmunsu.dingdongpang.domain.menu.controller.dto.MenuRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.menu.controller.dto.MenuUpdateRequest;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuListResponse;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuResponse;
import org.kwakmunsu.dingdongpang.global.annotation.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Menu API", description = "메뉴 관련 API 문서입니다.")
public abstract class MenuDocsController {

    @Operation(
            summary = "매장 메뉴 등록 API  - JWT O",
            description = """
                    ## 매장 메뉴 등록을 합니다. 이미 등록된 메뉴는 재등록이 불가능합니다.
                    **안내 사항**
                    - MenuRegisterRequest과 메뉴 이미지(image) 는 FormData로 보내주세요.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "매장 메뉴 등록 성공"
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            DUPLICATE,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> register(
            @Parameter(
                    description = "매장 메뉴 등록 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MenuRegisterRequest.class))
            )
            MenuRegisterRequest request,
            @Parameter(
                    description = "메뉴 이미지",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            MultipartFile image,
            Long memberId
    );

    @Operation(
            summary = "상인 전용 매장 메뉴 목록 조회 API  - JWT O",
            description = """
                    ## 상인 전용 매장 메뉴 목록 조회 API 입니다.
                    - 상인 모드일 떄 매장 메뉴를 조회할려면 해당 API를 호출하세요.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "매장 메뉴 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MenuListResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MenuListResponse> getMenusByMerchant(Long memberId);

    @Operation(
            summary = "고객 전용 매장 메뉴 목록 조회 API  - JWT O",
            description = """
                    ## 고객 전용 매장 메뉴 목록 조회 API 입니다.
                    - 괙 모드일 떄 매장 메뉴를 조회할려면 해당 API를 호출하세요.
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
            description = "매장 메뉴 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MenuListResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MenuListResponse> getMenusByCustomer(Long shopId);

    @Operation(
            summary = " 매장 메뉴 상세 조회 API  - JWT O",
            description = """
                    ## 매장 메뉴 상세 조회 API 입니다.
                    """
    )
    @Parameter(
            name = "menuId",
            description = "메뉴 id ",
            in = ParameterIn.PATH,
            required = true
    )
    @ApiResponse(
            responseCode = "200",
            description = "매장 메뉴 상세 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MenuResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MenuResponse> getMenu(Long menuId);


    @Operation(
            summary = "매장 메뉴 수정 API  - JWT O",
            description = """
                    ## 매장 메뉴 수정을 합니다. 매장 관리자가 아닐 경우 메뉴 수정은 불가능합니다.
                    **안내 사항**
                    - MenuUpdateRequest과 메뉴 이미지(image) 는 FormData로 보내주세요.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "매장 메뉴 수정 성공"
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> updateMenu(
            @Parameter(
                    description = "매장 메뉴 수정 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MenuUpdateRequest.class))
            )
            MenuUpdateRequest request,
            @Parameter(
                    description = "메뉴 수정 이미지",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            MultipartFile image,
            @Parameter(
                    name = "menuId",
                    description = "메뉴 id ",
                    in = ParameterIn.PATH,
                    required = true
            )
            Long menuId,
            Long memberId
    );
}