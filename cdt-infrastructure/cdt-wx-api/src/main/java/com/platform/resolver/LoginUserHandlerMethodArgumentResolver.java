package com.platform.resolver;

import com.platform.annotation.LoginUser;
import com.platform.entity.UserVo;
import com.platform.interceptor.AuthorizationInterceptor;
import com.platform.service.ApiUserService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 有@LoginUser注解的方法参数，注入当前登录用户
 *
 * @date 2017-03-23 22:02
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
