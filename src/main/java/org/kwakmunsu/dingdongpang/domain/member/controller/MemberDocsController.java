package org.kwakmunsu.dingdongpang.domain.member.controller;

import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.DUPLICATE;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.INVALID_TOKEN;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.kwakmunsu.dingdongpang.domain.auth.controller.dto.FcmTokenRequest;
import org.kwakmunsu.dingdongpang.domain.member.controller.dto.CustomerRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.member.controller.dto.MerchantRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CheckNicknameResponse;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CustomerProfileResponse;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.MerchantProfileResponse;
import org.kwakmunsu.dingdongpang.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member API", description = "회원 관련 API 문서입니다.")
public abstract class MemberDocsController {

    @Operation(
            summary = "고객 회원 요청 API  - JWT O",
            description = """
                    ## 고객 역할의 회원을 생성합니다.
                    """
    )
    @RequestBody(
            description = """
                    고객 회원 요청 정보
                    """,
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CustomerRegisterRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "고객 회원 생성 성공"
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            DUPLICATE,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> register(
            CustomerRegisterRequest request,
            Long memberId
    );

    @Operation(
            summary = "상인 회원 요청 API  - JWT O",
            description = """
                    ## 상인 등록과 매장 등록을 합니다.
                    **안내 사항**
                     - MerchantRegisterRequest, 매장 대표 이미지, 매장 이미지는 FormData로 보내주세요.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "상인 회원 생성 성공"
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
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MerchantRegisterRequest.class))
            )
            MerchantRegisterRequest request,
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
            summary = "닉네임 중복 확인 - JWT O"
    )
    @Parameter(
            name = "nickname",
            description = "중복 확인할 닉네임",
            in = ParameterIn.QUERY,
            required = true
    )
    @ApiResponse(
            responseCode = "204",
            description = "닉네임 중복 확인 성공"
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            DUPLICATE,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<CheckNicknameResponse> checkNameDuplicate(String nickname);

    @Operation(
            summary = "상인 모드 내 정보 도회 - JWT O"
    )
    @ApiResponse(
            responseCode = "200",
            description = "내 정보 확인 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MerchantProfileResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MerchantProfileResponse> getMerchantProfile(Long memberId);


    @Operation(
            summary = "고객 모드 내 정보 도회 - JWT O"
    )
    @ApiResponse(
            responseCode = "200",
            description = "내 정보 확인 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CustomerProfileResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<CustomerProfileResponse> getCustomerProfile(Long memberId);

    @Operation(
            summary = "Fcm 토큰 등록 API  - JWT O",
            description = """
                    ## FCM Token 등록 API 입니다.
                    - **로그인 후 사용자가 알림 허용을 했다면 해당 API를 통해 fcm 등록을 해주세요.**
                    """
    )
    @RequestBody(
            description = "Fcm 토큰 등록 요청",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FcmTokenRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "FCM token 등록 성공 "
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            INVALID_TOKEN,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> updateFcmToken(
            FcmTokenRequest request,
            Long memberId
    );
}