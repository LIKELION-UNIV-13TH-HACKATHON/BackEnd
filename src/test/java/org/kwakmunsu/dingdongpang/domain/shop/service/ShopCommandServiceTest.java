package org.kwakmunsu.dingdongpang.domain.shop.service;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shopoperation.ShopOperationTimeRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.OperationTimeServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shopimage.repository.ShopImageRepository;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.infrastructure.s3.S3Provider;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@SpringBootTest
record ShopCommandServiceTest(
        ShopCommandService shopCommandService,
        S3Provider s3Provider,
        ShopRepository shopRepository,
        ShopImageRepository shopImageRepository,
        ShopOperationTimeRepository shopOperationTimeRepository,
        EntityManager entityManager
) {
        @DisplayName("매장을 등록한다.")
        @Test
        void register() throws IOException {
            var shopRegisterServiceRequest = getShopRegisterServiceRequest();
            Point point = GeoFixture.createPoint(1.2, 2.3);

            shopCommandService.register(shopRegisterServiceRequest, point, 1L);

            boolean exists = shopRepository.existsByBusinessNumber(shopRegisterServiceRequest.businessNumber());

            assertThat(exists).isTrue();
        }

    private MockMultipartFile getMockMultipartFile() throws IOException {
        File image = new File("src/test/resources/test.png");
        return new MockMultipartFile(
                "image",                         // 파라미터 이름
                "test.png",                      // 파일 이름
                "image/png",                    // Content-Type
                new FileInputStream(image)
        );
    }

    private ShopRegisterServiceRequest getShopRegisterServiceRequest() throws IOException {
        MultipartFile mainImage = getMockMultipartFile();
        MultipartFile image1 = getMockMultipartFile();
        MultipartFile image2 = getMockMultipartFile();

        OperationTimeServiceRequest mondayOperation = new OperationTimeServiceRequest(
                DayOfWeek.MONDAY,
                "09:00",
                "22:00",
                false
        );
        OperationTimeServiceRequest tuesdayOperation = new OperationTimeServiceRequest(
                DayOfWeek.TUESDAY,
                "09:00",
                "22:00",
                false
        );
        return ShopRegisterServiceRequest.builder()
                .businessNumber("8962801461")    // 사업자 등록 번호
                .ownerName("김계란")              // 대표자명
                .shopName("역전할머니맥주")         // 매장명
                .shopType(ShopType.FASHION)      // 매장 타입 (enum)
                .shopPhoneNumber("010-8742-1234")// 매장 전화번호
                .address("경기도 광주시 경충대로1461번길 12-4 코오롱 세이브 프라자 202호")
                .mainImage(mainImage)
                .merchantId(1L)
                .imageFiles(List.of(image1, image2))
                .operationTimeRequests(List.of(mondayOperation, tuesdayOperation))
                .build();// 매장 주소
    }

}