package org.kwakmunsu.dingdongpang.domain.member.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.auth.controller.dto.FcmTokenRequest;
import org.kwakmunsu.dingdongpang.domain.member.controller.dto.CustomerRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.member.controller.dto.MerchantRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.MemberCommandService;
import org.kwakmunsu.dingdongpang.domain.member.service.MemberQueryService;
import org.kwakmunsu.dingdongpang.domain.member.service.MerchantOnboardingService;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CheckNicknameResponse;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CustomerProfileResponse;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.MerchantProfileResponse;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.FirebaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController extends MemberDocsController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final MerchantOnboardingService merchantOnboardingService;
    private final FirebaseService firebaseService;

    @Override
    @PostMapping("/customers")
    public ResponseEntity<Void> register(
            @Valid @RequestBody CustomerRegisterRequest request,
            @AuthMember Long memberId
    ) {
        memberCommandService.registerCustomer(request.toServiceRequest(memberId));

        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/merchants")
    public ResponseEntity<Void> register(
            @Valid @RequestPart MerchantRegisterRequest request,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @AuthMember Long memberId
    ) {
        merchantOnboardingService.register(request.toServiceRequest(mainImage, imageFiles, memberId));

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/check-nickname")
    public ResponseEntity<CheckNicknameResponse> checkNameDuplicate(@RequestParam String nickname) {
        CheckNicknameResponse response = memberQueryService.isExistsNickname(nickname);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/merchants/me")
    public ResponseEntity<MerchantProfileResponse> getMerchantProfile(@AuthMember Long memberId) {
        MerchantProfileResponse response = memberQueryService.getMerchantProfile(memberId);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/customers/me")
    public ResponseEntity<CustomerProfileResponse> getCustomerProfile(@AuthMember Long memberId) {
        CustomerProfileResponse response = memberQueryService.getCustomerProfile(memberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/fcm-token")
    public ResponseEntity<Void> updateFcmToken(
            @Valid @RequestBody FcmTokenRequest request,
            @AuthMember Long memberId
    ) {
        firebaseService.updateFcmToken(request.toServiceRequest(memberId));

        return ResponseEntity.ok().build();
    }

}