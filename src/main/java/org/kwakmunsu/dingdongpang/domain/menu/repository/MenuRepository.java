package org.kwakmunsu.dingdongpang.domain.menu.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MenuRepository {

    private final MenuJpaRepository menuJpaRepository;

}