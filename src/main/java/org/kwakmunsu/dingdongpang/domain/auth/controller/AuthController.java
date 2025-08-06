package org.kwakmunsu.dingdongpang.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.auth.controller.dto.ReissueTokenRequest;
import org.kwakmunsu.dingdongpang.domain.auth.service.AuthCommandService;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInResponse;
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
public class AuthController {

    private final AuthCommandService authCommandService;

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestHeader("Authorization") String socialAccessToken) {
        SignInResponse response = authCommandService.signIn(socialAccessToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@Valid @RequestBody ReissueTokenRequest request) {
        TokenResponse response = authCommandService.reissue(request.refreshToken());

        return ResponseEntity.ok(response);
    }

}