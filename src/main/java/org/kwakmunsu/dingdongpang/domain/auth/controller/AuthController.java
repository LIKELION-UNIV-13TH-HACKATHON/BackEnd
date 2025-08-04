package org.kwakmunsu.dingdongpang.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.auth.service.AuthCommandService;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

}