package org.kwakmunsu.dingdongpang.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.member.controller.dto.CustomerRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.MemberCommandService;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberCommandService memberCommandService;

    @PostMapping("/customers")
    public ResponseEntity<Void> register(
            @Valid @RequestBody CustomerRegisterRequest request,
            @AuthMember Long memberId
    ) {
        memberCommandService.registerCustomer(request.toServiceRequest(memberId));

        return ResponseEntity.ok().build();
    }

}