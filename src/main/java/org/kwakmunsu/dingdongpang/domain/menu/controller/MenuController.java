package org.kwakmunsu.dingdongpang.domain.menu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.menu.controller.dto.MenuRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.menu.service.MenuCommandService;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/shops")
@RequiredArgsConstructor
@RestController
public class MenuController extends MenuDocsController{

    private final MenuCommandService menuCommandService;

    @Override
    @PostMapping("/menus")
    public ResponseEntity<Void> register(
            @Valid @RequestPart MenuRegisterRequest request,
            @RequestPart(required = false) MultipartFile image,
            @AuthMember Long memberId
    ) {
        menuCommandService.register(request.toServiceRequest(image, memberId));

        return ResponseEntity.ok().build();
    }

}