package org.kwakmunsu.dingdongpang.global.annotation.resolver;

import static org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus.EMPTY_SECURITY_CONTEXT;

import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.kwakmunsu.dingdongpang.global.exception.UnAuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class) &&
                parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new UnAuthenticationException(EMPTY_SECURITY_CONTEXT);
        }

        return Long.parseLong(authentication.getName());
    }

}