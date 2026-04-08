package com.transport.tracking.k.config;

import com.transport.tracking.k.annotation.Anonymous;
import com.transport.tracking.k.exception.UNAuthorizedException;
import com.transport.tracking.k.service.TokenService;
import com.transport.tracking.response.AccessTokenVO;
import com.transport.tracking.response.UserVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class AuthRequestHeadersResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Method method = parameter.getMethod();
        return !method.isAnnotationPresent(Anonymous.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if(parameter.getParameterType().equals(AccessTokenVO.class)) {
            HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
            final Cookie cookie = WebUtils.getCookie(servletRequest, "token");
            if(Objects.nonNull(cookie)) {
                //return new AccessTokenVO();
                return this.getAccessTokenVO(cookie);
            }
        }
        return new AccessTokenVO();
    }

    private AccessTokenVO getAccessTokenVO(Cookie cookie) {
        try {
            Claims claims = tokenService.decodeAccessToken(cookie.getValue());

            AccessTokenVO accessTokenVO = new AccessTokenVO();
            accessTokenVO.setAccessToken(cookie.getValue());

            UserVO userVO = new UserVO();

            if(Objects.nonNull(claims.get("username"))) {
                userVO.setXusrname(claims.get("username").toString());
            }
/*
            if(Objects.nonNull(claims.get("site"))) {
                userVO.setSite(claims.get("site").toString());
            }
*/
            if(Objects.nonNull(claims.get("authorities"))) {
                List<String> permissions = (List) claims.get("authorities");
                accessTokenVO.setPermissions(permissions);
            }

            return accessTokenVO;
        }catch (Exception e) {
            log.error("User Unauthorized", e.getMessage());
        }
        throw new UNAuthorizedException(HttpStatus.UNAUTHORIZED.value(), "User is UNAuthorized");
    }
}
