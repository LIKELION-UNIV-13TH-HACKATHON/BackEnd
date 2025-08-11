package org.kwakmunsu.dingdongpang.domain.inquiry.controller;

import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.FORBIDDEN_ANSWER;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.FORBIDDEN_MODIFY;
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
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryAnswerRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryModifyRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListByMerchantResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListResponse;
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
            summary = "고객 전용 문의 목록 조회 API  - JWT O",
            description = """
                    ## 고객 모드일 때 매장에 대한 문의 목록을 조회합니다.
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
            description = "고객 전용 문의 목록 조회 성공",
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
    public abstract ResponseEntity<org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListResponse> getInquiryListByCustomer(
            InquiryFilter filter,
            Long shopId,
            Long memberId
    );

    @Operation(
            summary = "싱인 전용 문의 목록 조회 API  - JWT O",
            description = """
                    ## 싱인 모드일 때 매장에 대한 문의 목록을 조회합니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "상인 전용 문의 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InquiryListByMerchantResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<InquiryListByMerchantResponse> getInquiryListByMerchant(Long memberId);


    @Operation(
            summary = "문의 답변 등록 API  - JWT O",
            description = """
                    ## 매장 관리자만 등록이 가능합니다.
                    """
    )
    @Parameters(value = {
            @Parameter(
                    name = "shopId",
                    description = " 매장 ID",
                    in = ParameterIn.PATH,
                    required = true
            ),
            @Parameter(
                    name = "inquiryId",
                    description = "문의 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
    })
    @ApiResponse(
            responseCode = "200",
            description = "문의 답변 등록 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InquiryListResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            NOT_FOUND,
            FORBIDDEN_ANSWER,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> registerAnswer(
            InquiryAnswerRequest request,
            Long shopId,
            Long inquiryId,
            Long memberId
    );

    @Operation(
            summary = "문의 수정 API  - JWT O",
            description = """
                    ## 문의 작성자만이 수정 가능합니다.
                    """
    )
    @Parameter(
            name = "inquiryId",
            description = "문의 ID",
            in = ParameterIn.PATH,
            required = true
    )
    @RequestBody(
            description = """
                    문의 수정 정보
                    """,
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InquiryModifyRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "204",
            description = "문의 수정 성공"
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            FORBIDDEN_MODIFY,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> modifyQuestion(
            InquiryModifyRequest request,
            Long inquiryId,
            Long memberId
    );

}