package org.kwakmunsu.dingdongpang.domain.shopimage.repository;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShopImageBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveShopImagesBulk(List<String> uploadedImages, Long shopId) {
        String sql = "INSERT INTO shop_image (shop_id, image, created_at, updated_at) VALUES (?, ?, ?, ?)";

        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.batchUpdate(sql, uploadedImages, uploadedImages.size(),
                (ps, imageUrl) -> {
                    ps.setLong(1, shopId);
                    ps.setString(2, imageUrl);
                    ps.setObject(3, now);
                    ps.setObject(4, now);
                });
    }

}