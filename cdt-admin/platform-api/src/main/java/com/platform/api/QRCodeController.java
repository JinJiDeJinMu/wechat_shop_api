package com.platform.api;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaQrcodeServiceImpl;
import com.chundengtai.base.constant.CacheConstant;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.UserVo;
import com.platform.util.ApiBaseAction;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * **************************************************************
 * 公司名称    :
 * 系统名称    :
 * 类 名 称    :二维码获取
 * 功能描述    :会员登录
 * 业务描述    :会员登录
 * 作 者 名    :@Author Royal
 * 开发日期    :
 * **************************************************************
 * 修改日期    :
 * 修 改 者    :
 * 修改内容    :
 * **************************************************************
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class QRCodeController extends ApiBaseAction {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private WxMaService wxMaService;

    @RequestMapping("/getQRCode")
    public Object getQRCode(HttpServletRequest request, @LoginUser UserVo loginUser,
                            @RequestParam(value = "params", required = false) String params,
                            @RequestParam(value = "page", required = false) String page
    ) throws WxErrorException, IOException {
        Long shopUserId = loginUser.getUserId();
        params = shopUserId + "_" + params;

        Object imageName = redisTemplate.opsForValue().get(CacheConstant.QRCODE_CACHE + params);
        if (imageName != null) {
            return toResponsSuccess(imageName.toString());
        }
        WxMaQrcodeService service1 = new WxMaQrcodeServiceImpl(wxMaService);
        File image = service1.createWxaCodeUnlimit(params, page, 100, true, null, false);
        String resource = "/data/wwwroot/school.chundengtai.com/qrcode";
        // 将源文件拷贝到目标目录中，如果目标目录不存在，则创建
        FileUtils.copyFileToDirectory(image, new File(resource));
        redisTemplate.opsForValue().set(CacheConstant.QRCODE_CACHE + params, image.getName(), 15, TimeUnit.DAYS);
        return toResponsSuccess(image.getName());
    }

    @IgnoreAuth
    @RequestMapping("/getQRCodeTest")
    public Object getQRCodeTest(HttpServletRequest request,
                                @RequestParam(value = "params", required = false) String params,
                                @RequestParam(value = "page", required = false) String page
    ) throws WxErrorException, IOException {
        params = params;
        WxMaQrcodeService service1 = new WxMaQrcodeServiceImpl(wxMaService);
        File image = service1.createWxaCodeUnlimit(params, page, 100, true, null, false);
        String resource = "/data/wwwroot/school.chundengtai.com/qrcode";
//        String qrImag = "public/jpg/";
        // 将源文件拷贝到目标目录中，如果目标目录不存在，则创建
        FileUtils.copyFileToDirectory(image, new File(resource));
        return toResponsSuccess(image.getName());
    }
}
