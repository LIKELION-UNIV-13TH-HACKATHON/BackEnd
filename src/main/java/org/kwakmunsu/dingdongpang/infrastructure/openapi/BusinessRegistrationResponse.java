package org.kwakmunsu.dingdongpang.infrastructure.openapi;

import java.util.List;

public record BusinessRegistrationResponse(
        String status_code,
        int match_cnt,
        int request_cnt,
        List<BusinessData> data
) {
    public record BusinessData(
            String b_no,
            String b_stt,
            String b_stt_cd,
            String tax_type,
            String tax_type_cd,
            String end_dt,
            String utcc_yn,
            String tax_type_change_dt,
            String invoice_apply_dt,
            String rbf_tax_type,
            String rbf_tax_type_cd
    ) {

    }

}