package org.kwakmunsu.dingdongpang.domain.inquiry.controller;

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
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryListResponse;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopListResponse;
import org.kwakmunsu.dingdongpang.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Inquiry API", description = "문의 관련 API 문서입니다.")
public abstract class InquiryDocsController {

    @Operation(
            summary = "문의 등록 API  - JWT O",
            description = """
                    ## 매장에 대한 문의를 등록합니다.
                    """
    )
    @RequestBody(
            description = """
                    문의 등록 정보
                    """,
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InquiryRegisterRequest.class)
            )
    )
    @Parameter(
            name = "shopId",
            description = "문의 등록 할 매장 ID",
            in = ParameterIn.PATH,
            required = true
    )
    @ApiResponse(
            responseCode = "200",
            description = "문의 등록 성공"
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> register(
            InquiryRegisterRequest request,
            Long shopId,
            Long memberId
    );

    @Operation(
            summary = "문의 목록 조회 API  - JWT O",
            description = """
                    ## 매장에 대한 문의 목록을 조회합니다.
                    """
    )
    @Parameters(value = {
            @Parameter(
                    name = "shopId",
                    description = "조회 할 매장 ID",
                    in = ParameterIn.PATH,
                    required = true
            ),
            @Parameter(
                    name = "filter",
                    description = "문의 목록 필터",
                    in = ParameterIn.QUERY
            )
    })
    @ApiResponse(
            responseCode = "200",
            description = "문의 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InquiryListResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<InquiryListResponse> getInquiryList(
            InquiryFilter filter,
            Long shopId,
            Long memberId
    );

}