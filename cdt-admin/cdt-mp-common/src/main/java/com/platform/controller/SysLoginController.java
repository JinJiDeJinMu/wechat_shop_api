package com.platform.controller;

import com.chundengtai.base.annotation.SysLog;
import com.chundengtai.base.common.HutoolCaptcha;
import com.google.code.kaptcha.Constants;
import com.platform.utils.R;
import com.platform.utils.ShiroUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class SysLoginController {

    @RequestMapping("yanzhengma.do")
    public void captcha(HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        HutoolCaptcha simpleCaptcha = new HutoolCaptcha(200, 50, 4, 20);
        try {
            simpleCaptcha.write(response.getOutputStream());
            //保存到shiro session
            ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, simpleCaptcha.getCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     */
    @SysLog("登录")
    @ResponseBody
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public R login(String username, String password, String captcha) throws IOException {
        String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
        if(null == kaptcha){
            return R.error("验证码已失效");
        }
        if (!captcha.equalsIgnoreCase(kaptcha)) {
            return R.error("验证码不正确");
        }

        try {
            Subject subject = ShiroUtils.getSubject();
            //sha256加密
            password = new Sha256Hash(password).toHex();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        } catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return R.error(e.getMessage());
        } catch (LockedAccountException e) {
            return R.error(e.getMessage());
        } catch (AuthenticationException e) {
            return R.error("账户验证失败");
        }

        return R.ok();
    }

    /**
     * 退出
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        ShiroUtils.logout();
        return "redirect:/";
    }

}
