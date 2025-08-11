package org.kwakmunsu.dingdongpang.domain.inquiry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.InquiryCommandService;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.InquiryQueryService;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryListResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryReadServiceRequest;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/{shopId}/inquiries")
    public ResponseEntity<InquiryListResponse> getInquiryList(
            @RequestParam(defaultValue = "GENERAL") InquiryFilter filter,
            @PathVariable Long shopId,
            @AuthMember Long memberId
    ) {
        InquiryReadServiceRequest request = new InquiryReadServiceRequest(filter, shopId, memberId);
        InquiryListResponse response = inquiryQueryService.getInquiryList(request);

        return ResponseEntity.ok(response);
    }

}