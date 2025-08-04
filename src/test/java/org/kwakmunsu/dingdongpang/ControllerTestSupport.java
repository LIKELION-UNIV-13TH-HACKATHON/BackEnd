package org.kwakmunsu.dingdongpang;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kwakmunsu.dingdongpang.domain.auth.controller.AuthController;
import org.kwakmunsu.dingdongpang.domain.auth.service.AuthCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@Import(TestSecurityConfig.class)
@WebMvcTest(
        controllers = {
                AuthController.class,
        })
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvcTester mvcTester;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected AuthCommandService authCommandService;

}