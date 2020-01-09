package com.platform.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.User;
import com.chundengtai.base.facade.IdistributionFacade;
import com.chundengtai.base.service.UserService;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.LoginInfo;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserService;
import com.platform.service.TokenService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiUserUtils;
import com.platform.utils.Base64;
import com.qiniu.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(value = "未名严选", tags = "未名严选")
@RestController
@RequestMapping("/apis/v2/auth")
@Slf4j
public class WxUserController extends ApiBaseAction {
    @Autowired
    private IdistributionFacade idistributionFacade;

    @Autowired
    private ApiUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    UserService newUserService;

    /**
     * 登录
     */
    @ApiOperation(value = "登录")
    @IgnoreAuth
    @PostMapping("wxLogin.do")
    public Object loginByWeixin(@RequestBody LoginInfo loginInfo, HttpServletRequest request) {
        log.info(" testAsync 当前线程id:" + Thread.currentThread().getId() + ", 当前线程名称:" + Thread.currentThread().getName());

        //获取openid
        String requestUrl = ApiUserUtils.getWebAccess(loginInfo.getCode());
        //通过自定义工具类组合出小程序需要的登录凭证 code
        String res = restTemplate.getForObject(requestUrl, String.class);
        JSONObject sessionData = JSON.parseObject(res);
        String openid = sessionData.getString("openid");
        log.info("》》》promoterId：" + loginInfo.getPromoterId());
        String session_key = sessionData.getString("session_key");//不知道啥用。
        if (null == sessionData || StringUtils.isNullOrEmpty(openid)) {
            return toResponsFail("登录失败");
        }

        //验证用户信息完整性 防止攻击
//        String sha1 = CommonUtil.getSha1(fullUserInfo.getRawData() + sessionData.getString("session_key"));
//        if (!fullUserInfo.getSignature().equals(sha1)) {
//        	 logger.info("登录失败---验证用户信息完整性"+fullUserInfo.getSignature());
//            return toResponsFail("登录失败");
//        }
        User userVo = newUserService.getOne(new QueryWrapper<User>().lambda().eq(User::getWeixinOpenid, openid));
        if (null == userVo) {
            userVo = new User();
            userVo.setUsername(Base64.encode(loginInfo.getNickName()));
            userVo.setPassword(openid);
            userVo.setRegisterTime(new Date());
            userVo.setRegisterIp(this.getClientIp());
            userVo.setLastLoginIp(userVo.getRegisterIp());
            userVo.setLastLoginTime(userVo.getRegisterTime());
            userVo.setWeixinOpenid(openid);
            userVo.setAvatar(loginInfo.getAvatarUrl());
            userVo.setGender(loginInfo.getGender()); // //性别 0：未知、1：男、2：女
            userVo.setNickname(Base64.encode(loginInfo.getNickName()));
            //判断是否是推广用户登录
            if (loginInfo.getPromoterId() != 0) {//非邀请用户
                userVo.setPromoterId(loginInfo.getPromoterId());
                UserVo userVo1 = userService.queryObject(new Long(loginInfo.getPromoterId()));
                if (userVo1 != null) {
                    userVo.setPromoterName(userVo1.getUsername());
                }
            }
            boolean result = newUserService.save(userVo);
        }
        //生成推广二维码
//        if(StringUtils.isNullOrEmpty(userVo.getQrCode()) ){
//            UserVo uVo=new UserVo();
//            String accessToken = tokenService.getAccessToken() ;
//            uVo.setUserId(userVo.getUserId());
//            uVo.setQrCode(QRCodeUtils.getminiqrQr(accessToken,request,String.valueOf(userVo.getUserId())));
//            userService.update(uVo);
//        }

        Map<String, Object> tokenMap = tokenService.createToken(userVo.getId());
        String token = MapUtils.getString(tokenMap, "token");
        if (StringUtils.isNullOrEmpty(token)) {
            return toResponsFail("登录失败");
        }

        if (!StringUtils.isNullOrEmpty(loginInfo.getReferrerId())) {
            idistributionFacade.referreRelation(userVo.getId(), loginInfo.getReferrerId());
        }
        Map<String, Object> resultObj = new HashMap<String, Object>();
        resultObj.put("userVo", userVo);
        return toResponsSuccess(resultObj);
    }
}
