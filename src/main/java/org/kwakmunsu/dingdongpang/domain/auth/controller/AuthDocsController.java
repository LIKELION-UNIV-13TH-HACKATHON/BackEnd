package org.kwakmunsu.dingdongpang.domain.auth.controller;

import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.INVALID_TOKEN;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.NOT_FOUND_TOKEN;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.dingdongpang.domain.auth.controller.dto.ReissueTokenRequest;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInResponse;
import org.kwakmunsu.dingdongpang.global.annotation.ApiExceptions;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth API", description = "인증 관련 API 문서입니다.")
public abstract class AuthDocsController {

    @Operation(
            summary = "소셜 로그인 요청  - JWT O",
            description = """
                    ## 카카오 소셜 서버에서 받은 AccessToken을 Header에 담아 요청 시 검증 후 서버 자체 Access, Refresh Token을 발급합니다.
                    - **기존 회원이 로그인 할 경우**
                      - 홈 화면으로 이동
                    - **새로운 회원이 로그인 할 경우**
                      - 정보 등록 화면으로 이동
                    """
    )
    @Parameter(
            name = "Authorization",
            description = "소셜 로그인 액세스 토큰 (Bearer ...)",
            required = true,
            example = "Bearer eyJ..."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공 ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SignInResponse.class)
            ))
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<SignInResponse> signIn(String socialAccessToken);

    @Operation(
            summary = "Access, Refresh Token 재발급 요청 - JWT O",
            description = "Refresh Token을 이용해 새로운 Access Token 및 Refresh Token을 재발급합니다."
    )
    @RequestBody(
            description = "토큰 재발급 요청",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ReissueTokenRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Access, Refresh Token 재발급 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TokenResponse.class)
            )
    )
    @ApiExceptions(values = {
            INVALID_TOKEN,
            NOT_FOUND_TOKEN,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<TokenResponse> reissue(ReissueTokenRequest request);

}