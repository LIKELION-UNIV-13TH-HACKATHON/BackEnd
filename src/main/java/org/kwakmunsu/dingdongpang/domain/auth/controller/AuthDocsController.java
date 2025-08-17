package org.kwakmunsu.dingdongpang.domain.auth.controller;

import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.INVALID_TOKEN;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.NOT_FOUND_TOKEN;
import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.dingdongpang.domain.auth.controller.dto.ReissueTokenRequest;
import org.kwakmunsu.dingdongpang.domain.auth.controller.dto.SignInRequest;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInResponse;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;
import org.kwakmunsu.dingdongpang.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth API", description = "인증 관련 API 문서입니다.")
public abstract class AuthDocsController {

    @Operation(
            summary = "소셜 로그인 요청  - JWT O",
            description = """
                    - **기존 회원이 로그인 할 경우**
                      - 홈 화면으로 이동
                    - **새로운 회원이 로그인 할 경우**
                      - 정보 등록 화면으로 이동
                    """
    )
    @RequestBody(
            description = "로그인 요청",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SignInRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공 ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SignInResponse.class))
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<SignInResponse> signIn(SignInRequest signInRequest);

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

    @Operation(
            summary = "로그아웃 요청 - JWT O",
            description = "로그아웃시 서버는 FCM 토큰과 RefreshToken을 초기화합니다. 프론트는 저장한 AccessToken과 RefreshToken을 제거해주세요."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그아웃 성공"
    )
    @ApiExceptions(values = {
            INVALID_TOKEN,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> signOut(@AuthMember Long memberId);

}