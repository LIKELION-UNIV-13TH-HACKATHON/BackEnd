package org.kwakmunsu.dingdongpang.domain.inquiry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryAnswerModifyRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryAnswerRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryModifyRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryStatus;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.InquiryCommandService;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.InquiryQueryService;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryDeleteServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryReadByMerchantServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryReadServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListByMerchantResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListResponse;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/shops")
@RequiredArgsConstructor
@RestController
public class InquiryController extends InquiryDocsController {

    private final InquiryCommandService inquiryCommandService;
    private final InquiryQueryService inquiryQueryService;

    @Override
    @PostMapping("/{shopId}/inquiries")
    public ResponseEntity<Void> register(
            @Valid @RequestBody InquiryRegisterRequest request,
            @PathVariable Long shopId,
            @AuthMember Long memberId
    ) {
        inquiryCommandService.register(request.toServiceRequest(shopId, memberId));

        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/{shopId}/inquiries/{inquiryId}/answer")
    public ResponseEntity<Void> registerAnswer(
            @Valid @RequestBody InquiryAnswerRequest request,
            @PathVariable Long shopId,
            @PathVariable Long inquiryId,
            @AuthMember Long memberId
    ) {
        inquiryCommandService.registerAnswer(request.toServiceRequest(shopId, inquiryId, memberId));

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{shopId}/inquiries/customers")
    public ResponseEntity<InquiryListResponse> getInquiryListByCustomer(
            @RequestParam(defaultValue = "GENERAL") InquiryFilter filter,
            @PathVariable Long shopId,
            @AuthMember Long memberId
    ) {
        InquiryReadServiceRequest request = new InquiryReadServiceRequest(filter, shopId, memberId);
        InquiryListResponse response = inquiryQueryService.getInquiryList(request);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/inquiries/merchants")
    public ResponseEntity<InquiryListByMerchantResponse> getInquiryListByMerchant(
            @AuthMember Long memberId,
            @RequestParam(defaultValue = "GENERAL") InquiryStatus status
    ) {
        InquiryReadByMerchantServiceRequest request = new InquiryReadByMerchantServiceRequest(
                memberId, status);
        InquiryListByMerchantResponse response = inquiryQueryService.getInquiryListByMerchant(request);

        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/inquiries/{inquiryId}")
    public ResponseEntity<Void> modifyInquiry(
            @Valid @RequestBody InquiryModifyRequest request,
            @PathVariable Long inquiryId,
            @AuthMember Long memberId
    ) {
        inquiryCommandService.modifyInquiry(request.toServiceRequest(inquiryId, memberId));

        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{shopId}/inquiries/{inquiryId}/answer")
    public ResponseEntity<Void> modifyAnswer(
            @Valid @RequestBody InquiryAnswerModifyRequest request,
            @PathVariable Long shopId,
            @PathVariable Long inquiryId,
            @AuthMember Long memberId
    ) {
        inquiryCommandService.modifyAnswer(request.toServiceRequest(shopId, inquiryId, memberId));

        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{shopId}/inquiries/{inquiryId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long shopId,
            @PathVariable Long inquiryId,
            @AuthMember Long memberId
    ) {
        InquiryDeleteServiceRequest request = new InquiryDeleteServiceRequest(shopId, inquiryId, memberId);
        inquiryCommandService.delete(request);

        return ResponseEntity.noContent().build();
    }

}