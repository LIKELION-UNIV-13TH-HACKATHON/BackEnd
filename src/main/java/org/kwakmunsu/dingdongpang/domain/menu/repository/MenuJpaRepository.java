package org.kwakmunsu.dingdongpang.domain.menu.repository;

import java.util.List;
import org.kwakmunsu.dingdongpang.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpaRepository extends JpaRepository<Menu, Long> {

    boolean existsByShopIdAndName(Long shopId, String name);

    List<Menu> findByShopId(Long shopId);

}