package com.chundengtai.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.entity.LoginInfo;
import com.chundengtai.base.entity.UserVo;
import com.chundengtai.base.facade.IdistributionFacade;
import com.chundengtai.base.service.ApiUserService;
import com.chundengtai.base.service.MlsUserSer;
import com.chundengtai.base.service.TokenService;
import com.chundengtai.base.util.ApiBaseAction;
import com.chundengtai.base.util.ApiUserUtils;
import com.chundengtai.base.utils.Base64;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.validator.Assert;
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

/**
 * API登录授权
 */
@Api(tags = "API登录授权接口")
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class ApiAuthController extends ApiBaseAction {

    @Autowired
    private IdistributionFacade idistributionFacade;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MlsUserSer mlsUserSer;

    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping("login")
    @ApiOperation(value = "登录接口")
    public R login(String mobile, String password) {
        Assert.isBlank(mobile, "手机号不能为空");
        Assert.isBlank(password, "密码不能为空");

        //用户登录
        long userId = userService.login(mobile, password);

        //生成token
        Map<String, Object> map = tokenService.createToken(userId);

        return R.ok(map);
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录")
    @IgnoreAuth
    @PostMapping("login_by_weixin")
    public Object loginByWeixin(@RequestBody LoginInfo loginInfo, HttpServletRequest request) {
        log.info(" loginByWeixin 当前线程id:" + Thread.currentThread().getId() + ", 当前线程名称:" + Thread.currentThread().getName());

        //获取openid
        String requestUrl = ApiUserUtils.getWebAccess(loginInfo.getCode());//通过自定义工具类组合出小程序需要的登录凭证 code
        //log.info("》》》组合token为：" + requestUrl);
        String res = restTemplate.getForObject(requestUrl, String.class);
        //log.info("res==" + res);
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
//        	 logger.info("登录失败---验证用户信息完整性 sha1"+sha1);
//            return toResponsFail("登录失败");
//        }
        Date nowTime = new Date();
        UserVo userVo = userService.queryByOpenId(openid);
        if (null == userVo) {
            userVo = new UserVo();

            userVo.setUsername(Base64.encode(loginInfo.getNickName()));
            userVo.setPassword(openid);
            userVo.setRegister_time(nowTime);
            userVo.setRegister_ip(this.getClientIp());
            userVo.setLast_login_ip(userVo.getRegister_ip());
            userVo.setLast_login_time(userVo.getRegister_time());
            userVo.setWeixin_openid(openid);
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
            userService.save(userVo);
//            MlsUserEntity2 fmlsUser = new MlsUserEntity2();
//            if (loginInfo.getPromoterId() != 0) {
//                MlsUserEntity2 mlsuser = this.mlsUserSer.getEntityMapper().findByUserId(new Long(loginInfo.getPromoterId()));
//                fmlsUser.setFid(mlsuser.getMlsUserId());
//            }
//            fmlsUser.setUserId(userVo.getUserId());
//            fmlsUser.setNickname(userVo.getUsername());
//            fmlsUser.setLoginPassword(userVo.getAvatar());//放头像了
//            this.mlsUserSer.insUser(fmlsUser);

//            if(loginInfo.getPromoterId()!=0) {//非邀请用户
//            	MlsUserEntity2 mlsUser = new MlsUserEntity2 ();
//            	mlsUser.setUserId(Long.valueOf(loginInfo.getPromoterId()));
//            	mlsUser.setNickname(userVo.getUsername());
//            	mlsUser.setLoginPassword(userVo.getAvatar());//放头像了
//        		List<MlsUserEntity2> userList = mlsUserSer.getEntityMapper().findByEntity(mlsUser);
//        		if(userList != null && userList.size() > 0) {
//        			MlsUserEntity2 fmlsUser= userList.get(0);
//					mlsUser.setFid(fmlsUser.getMlsUserId());
//					mlsUser.setFx(fmlsUser.getFx());
//					mlsUser.setFx1(fmlsUser.getFx1());
//					mlsUser.setFx2(fmlsUser.getFx2());
//					mlsUser.setPfx(fmlsUser.getPfx());
//					mlsUser.setMerchantId(fmlsUser.getMerchantId());
//					mlsUser.setUserId(userVo.getUserId());
//					mlsUser.setRootId(fmlsUser.getRootId());
//        		}else {
//					mlsUser.setFid(null);
//					Map<String,Object> map=mlsUserSer.getEntityMapper().getSysUser(loginInfo.getMerchantId());
//					mlsUser.setFx(Double.valueOf(map.get("FX").toString()));
//					mlsUser.setFx1(Double.valueOf(map.get("FX1").toString()));
//					mlsUser.setFx2(Double.valueOf(map.get("FX2").toString()));
//					mlsUser.setPfx(Double.valueOf(map.get("PFX").toString()));
//					mlsUser.setUserId(userVo.getUserId());
//					mlsUser.setMerchantId(loginInfo.getMerchantId());
//				}
//        		mlsUser.setDeviceId(openid);
//        		mlsUserSer.insUser(mlsUser);
//    			if(mlsUser.getRootId()==null) {
//    				mlsUserSer.getEntityMapper().updateRootId(mlsUser);
//    			}
//            }
        }
        //生成推广二维码
//        if(StringUtils.isNullOrEmpty(userVo.getQrCode()) ){
//            UserVo uVo=new UserVo();
//            String accessToken = tokenService.getAccessToken() ;
//            uVo.setUserId(userVo.getUserId());
//            uVo.setQrCode(QRCodeUtils.getminiqrQr(accessToken,request,String.valueOf(userVo.getUserId())));
//            userService.update(uVo);
//        }

        Map<String, Object> tokenMap = tokenService.createToken(userVo.getUserId());
        String token = MapUtils.getString(tokenMap, "token");

        if (StringUtils.isNullOrEmpty(token)) {
            return toResponsFail("登录失败");
        }

        if (!StringUtils.isNullOrEmpty(loginInfo.getReferrerId())) {
            log.info(" loginByWeixin 当前线程id:" + Thread.currentThread().getId() + ", 当前线程名称:" + Thread.currentThread().getName());
            idistributionFacade.referreRelation(userVo.getUserId(), loginInfo.getReferrerId());
        }
        Map<String, Object> resultObj = new HashMap<String, Object>();
        //resultObj.put("openid", openid);
        resultObj.put("userVo", userVo);
        return toResponsSuccess(resultObj);
    }

}
