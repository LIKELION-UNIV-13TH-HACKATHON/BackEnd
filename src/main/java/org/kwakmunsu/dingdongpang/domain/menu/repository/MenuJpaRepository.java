package org.kwakmunsu.dingdongpang.domain.menu.repository;

import org.kwakmunsu.dingdongpang.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpaRepository extends JpaRepository<Menu, Long> {

}