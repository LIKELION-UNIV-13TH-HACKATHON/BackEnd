package org.kwakmunsu.dingdongpang.domain.shop.repository.shopoperation;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopOperationTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShopOperationTimeBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveOperationTimesBulk(List<ShopOperationTime> operationTimes, Long shopId) {
        if (operationTimes.isEmpty()) return;

        String sql = """
            INSERT INTO shop_operation_time (shop_id, day_of_week, open_time, close_time, is_closed, created_at, updated_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.batchUpdate(sql, operationTimes, operationTimes.size(),
                (ps, ot) -> {
                    ps.setLong(1, shopId);
                    ps.setString(2, ot.getDayOfWeek().toString());
                    ps.setObject(3, ot.getOpenTime());
                    ps.setObject(4, ot.getCloseTime());
                    ps.setBoolean(5, ot.isClosed());
                    ps.setObject(6, now);
                    ps.setObject(7, now);
                });
    }

}