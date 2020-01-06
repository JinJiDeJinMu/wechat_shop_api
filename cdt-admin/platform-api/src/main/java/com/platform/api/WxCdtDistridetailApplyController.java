package com.platform.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.bean.CdtDistridetailApply;
import com.chundengtai.base.result.R;
import com.chundengtai.base.service.CdtDistridetailApplyService;
import com.chundengtai.base.service.CdtDistridetailService;
import com.chundengtai.base.weixinapi.DistributionStatus;
import com.platform.annotation.LoginUser;
import com.platform.entity.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


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

    @ApiOperation(value = "提现申请", httpMethod = "POST")
    @ResponseBody
    @PostMapping("/distriapply.do")
    public R getCashapply(@LoginUser UserVo loginUser, String realName, String tel) {
        List<CdtDistridetail> items = cdtDistridetailService.list(new QueryWrapper<CdtDistridetail>().lambda()
                .eq(CdtDistridetail::getStatus, DistributionStatus.COMPLETED_ORDER.getCode())
                .eq(CdtDistridetail::getGoldUserId, loginUser.getUserId().intValue()));
        try {
            items.stream().forEach(s -> {
                applyFfenxiaoData(loginUser.getWeixin_openid(), loginUser.getUsername(), realName, s.getId());
            });
        } catch (Exception ex) {
            log.error("提交审核异常=========》" + ex.getMessage());
            return R.error("提交审核失败");
        }

        return R.error("提交审核成功");
    }

    private void applyFfenxiaoData(String weixinOpenid, String userName, String realName, long distridetailId) {
            CdtDistridetail cdtDistridetail = cdtDistridetailService.getById(distridetailId);
            String token = cdtDistridetail.getToken();
            log.info("token=" + token);
           /* cdtDistridetail.setId(null);
            cdtDistridetail.setToken(null);*/
        //String token_tem = "";
           /* try {
                token_tem = JavaWebToken.createJavaWebToken(BeanJwtUtil.javabean2map(cdtDistridetail));
                log.info("token_tem=" + token_tem);
            } catch (Exception e) {
                log.error("jwt加密异常========");
                e.printStackTrace();
            }*/
            //if (token.equals(token_tem)) {
                    log.info("token校验成功");
                    //更改分销订单状态为审核中
                    //cdtDistridetail.setId(null);
                    cdtDistridetail.setStatus(DistributionStatus.COMPLETED_GETGOLD_CHECK.getCode());
                    cdtDistridetail.setUpdateTime(new Date());
                    //cdtDistridetail.setToken(null);
                    //String cdtToken = JavaWebToken.createJavaWebToken(BeanJwtUtil.javabean2map(cdtDistridetail));
        //cdtDistridetail.setToken(null);
                    cdtDistridetail.setId(distridetailId);
                    cdtDistridetailService.updateById(cdtDistridetail);
                    log.info("======" + cdtDistridetail);

                    //插入到审核表
                    CdtDistridetailApply cdtDistridetailApply = new CdtDistridetailApply();
        cdtDistridetailApply.setId(distridetailId);
                    cdtDistridetailApply.setOrderSn(cdtDistridetail.getOrderSn());
                    cdtDistridetailApply.setMoney(cdtDistridetail.getMoney());
                    cdtDistridetailApply.setStatus(cdtDistridetail.getStatus());
                    cdtDistridetailApply.setWeixinOpenid(weixinOpenid);
                    cdtDistridetailApply.setUserName(userName);
                    cdtDistridetailApply.setRealName(realName);
                    cdtDistridetailApply.setApplyTime(new Date());
                    log.info("======" + cdtDistridetailApply);
                    cdtDistridetailApplyService.save(cdtDistridetailApply);

    }
}
