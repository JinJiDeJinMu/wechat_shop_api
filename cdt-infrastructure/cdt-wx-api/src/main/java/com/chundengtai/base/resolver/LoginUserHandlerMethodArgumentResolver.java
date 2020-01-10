package com.chundengtai.base.resolver;

import com.chundengtai.base.annotation.LoginUser;
import com.chundengtai.base.entity.UserVo;
import com.chundengtai.base.interceptor.AuthorizationInterceptor;
import com.chundengtai.base.service.ApiUserService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 有@LoginUser注解的方法参数，注入当前登录用户
 */
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }

    private ApiUserService userService;

    public void setUserService(ApiUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserVo.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        //获取用户ID
        UserVo user = (UserVo) request.getAttribute(AuthorizationInterceptor.LOGIN_USER_KEY, RequestAttributes.SCOPE_REQUEST);

//        user = (UserVo) redisTemplate.opsForValue().get(CacheConstant.USER + user.getUserId());
//        if (user == null) {
//            //获取用户信息
//            user = userService.queryObject((Long) object);
//            redisTemplate.opsForValue().set(RedisKey.USER + object.toString(), user, 12, TimeUnit.HOURS);
//        }

        if (user == null) {
            return null;
        }
        //获取用户信息
//        UserVo user = userService.queryObject((Long) object);

        return user;
    }
}
