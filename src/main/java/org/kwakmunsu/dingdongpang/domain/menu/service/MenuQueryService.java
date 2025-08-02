package org.kwakmunsu.dingdongpang.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MenuQueryService {

    private final MenuRepository menuRepository;

}