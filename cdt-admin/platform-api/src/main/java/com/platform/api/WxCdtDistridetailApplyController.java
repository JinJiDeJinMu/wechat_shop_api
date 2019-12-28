package com.platform.api;

import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.bean.CdtDistridetailApply;
import com.chundengtai.base.jwt.JavaWebToken;
import com.chundengtai.base.result.R;
import com.chundengtai.base.service.CdtDistridetailApplyService;
import com.chundengtai.base.service.CdtDistridetailService;
import com.chundengtai.base.utils.BeanJwtUtil;
import com.platform.annotation.IgnoreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Description 分销提现申请接口
 * @Author hujinbiao
 * @Date 2019年12月28日 0028 下午 02:06:57
 * @Version 1.0
 **/
@Api(tags = "分销提现申请")
@RestController
@RequestMapping("/apis/v2/distridetailapply")
@Slf4j
public class WxCdtDistridetailApplyController {

    @Autowired
    private CdtDistridetailService cdtDistridetailService;

    @Autowired
    private CdtDistridetailApplyService cdtDistridetailApplyService;

    @ApiOperation(value = "提现申请接口", httpMethod = "POST")
    @PostMapping("/distridetailapply.do")
    @ResponseBody
    @IgnoreAuth
    public R apply(String weixinOpenid, String userName, String realName, Long distridetailId) {

        if (cdtDistridetailApplyService.getById(distridetailId) != null) {
            return R.error("重复提交");
        }
        CdtDistridetail cdtDistridetail = cdtDistridetailService.getById(distridetailId);
        String token = cdtDistridetail.getToken();
        log.info("token=" + token);
        System.out.println(token);
        cdtDistridetail.setId(null);
        cdtDistridetail.setToken(null);
        String token_tem = "";
        try {
            token_tem = JavaWebToken.createJavaWebToken(BeanJwtUtil.javabean2map(cdtDistridetail));
            log.info("token_tem=" + token_tem);
        } catch (Exception e) {
            log.error("jwt加密异常========");
            e.printStackTrace();
        }
        if (token.equals(token_tem)) {
            CdtDistridetailApply cdtDistridetailApply = new CdtDistridetailApply();
            cdtDistridetailApply.setId(distridetailId);
            cdtDistridetailApply.setOrderSn(cdtDistridetail.getOrderSn());
            cdtDistridetailApply.setMoney(cdtDistridetail.getMoney());
            cdtDistridetailApply.setStatus(cdtDistridetail.getStatus());
            cdtDistridetailApply.setWeixinOpenid(weixinOpenid);
            cdtDistridetailApply.setUserName(userName);
            cdtDistridetailApply.setRealName(realName);
            cdtDistridetailApply.setApplyTime(new Date());
            cdtDistridetailApplyService.save(cdtDistridetailApply);
            return R.ok("提交审核成功");
        }
        return R.error("提交审核失败");
    }

}
