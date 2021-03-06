package com.chundengtai.base.resolver;

import com.chundengtai.base.annotation.APPLoginUser;
import com.chundengtai.base.entity.MlsUserEntity2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 有@LoginUser注解的方法参数，注入当前登录用户
 */
@Slf4j
public class AppLoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(MlsUserEntity2.class) && parameter.hasParameterAnnotation(APPLoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        //获取用户ID


        log.error("========MlsUserEntity2此处已经注释掉了-----");
        //MlsUserEntity2 user = (MlsUserEntity2) request.getAttribute(AuthorizationInterceptor.LOGIN_USER_KEY, RequestAttributes.SCOPE_REQUEST);
        //if (user == null) {
        return null;
        //}
        //获取用户信息
//        UserVo user = userService.queryObject((Long) object);
        //return user;
    }
}
