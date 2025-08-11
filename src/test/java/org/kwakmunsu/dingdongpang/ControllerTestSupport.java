package org.kwakmunsu.dingdongpang;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kwakmunsu.dingdongpang.domain.auth.controller.AuthController;
import org.kwakmunsu.dingdongpang.domain.auth.service.AuthCommandService;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.InquiryController;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.InquiryCommandService;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.InquiryQueryService;
import org.kwakmunsu.dingdongpang.domain.member.controller.MemberController;
import org.kwakmunsu.dingdongpang.domain.member.service.MemberCommandService;
import org.kwakmunsu.dingdongpang.domain.member.service.MemberQueryService;
import org.kwakmunsu.dingdongpang.domain.member.service.MerchantOnboardingService;
import org.kwakmunsu.dingdongpang.domain.menu.controller.MenuController;
import org.kwakmunsu.dingdongpang.domain.menu.service.MenuCommandService;
import org.kwakmunsu.dingdongpang.domain.menu.service.MenuQueryService;
import org.kwakmunsu.dingdongpang.domain.message.controller.MessageController;
import org.kwakmunsu.dingdongpang.domain.message.service.MessageService;
import org.kwakmunsu.dingdongpang.domain.shop.controller.ShopController;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopQueryService;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.controller.SubscribeController;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.service.SubscribeShopCommandService;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.service.SubscribeShopQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = {
                AuthController.class,
                MemberController.class,
                MenuController.class,
                ShopController.class,
                MessageController.class,
                SubscribeController.class,
                InquiryController.class,
        })
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvcTester mvcTester;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected AuthCommandService authCommandService;

    @MockitoBean
    protected MemberCommandService memberCommandService;

    @MockitoBean
    protected MemberQueryService memberQueryService;

    @MockitoBean
    protected MerchantOnboardingService merchantOnboardingService;

    @MockitoBean
    protected MenuCommandService menuCommandService;

    @MockitoBean
    protected MenuQueryService menuQueryService;

    @MockitoBean
    protected ShopCommandService shopCommandService;

    @MockitoBean
    protected ShopQueryService shopQueryService;

    @MockitoBean
    protected MessageService messageService;

    @MockitoBean
    protected SubscribeShopCommandService subscribeShopCommandService;

    @MockitoBean
    protected SubscribeShopQueryService subscribeShopQueryService;

    @MockitoBean
    protected InquiryCommandService inquiryCommandService;

    @MockitoBean
    protected InquiryQueryService inquiryQueryService;

}