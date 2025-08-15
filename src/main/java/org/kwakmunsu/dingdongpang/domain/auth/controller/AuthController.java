package org.kwakmunsu.dingdongpang.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.auth.controller.dto.ReissueTokenRequest;
import org.kwakmunsu.dingdongpang.domain.auth.service.AuthCommandService;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInResponse;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController extends AuthDocsController {

    private final AuthCommandService authCommandService;

    @Override
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(
            @RequestHeader("Authorization") String socialAccessToken,
            @RequestHeader(value = "FCM-Token", required = false) String fcmToken
    ) {
        SignInResponse response = authCommandService.signIn(socialAccessToken, fcmToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sing-out")
    public ResponseEntity<Void> signOut(@AuthMember Long memberId) {
        authCommandService.signOut(memberId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@Valid @RequestBody ReissueTokenRequest request) {
        TokenResponse response = authCommandService.reissue(request.refreshToken());

        return ResponseEntity.ok(response);
    }

}