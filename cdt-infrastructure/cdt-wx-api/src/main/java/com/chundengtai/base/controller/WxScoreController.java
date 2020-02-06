package com.chundengtai.base.controller;

import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.annotation.LoginUser;
import com.chundengtai.base.bean.CdtScore;
import com.chundengtai.base.bean.CdtScoreFlow;
import com.chundengtai.base.entity.UserVo;
import com.chundengtai.base.jwt.JavaWebToken;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.CdtScoreFlowService;
import com.chundengtai.base.service.CdtScoreService;
import com.chundengtai.base.util.ApiBaseAction;
import com.chundengtai.base.util.CommonUtil;
import com.chundengtai.base.utils.BeanJwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 购买积分
 * @Author hujinbiao
 * @Date 2020年1月14日 0014 下午 04:31:34
 * @Version 1.0
 **/
@Api(tags = "积分相关")
@RestController
@RequestMapping("/apis/v2/score")
@Slf4j
public class WxScoreController extends ApiBaseAction {

    @Autowired
    private CdtScoreService cdtScoreService;

    @Autowired
    private CdtScoreFlowService cdtScoreFlowService;


    @GetMapping("/score.json")
    @IgnoreAuth
    public Result ScoreList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageHelper.startPage(page, size);
        List<CdtScore> cdtScoreList = cdtScoreService.list();
        PageInfo pageInfo = new PageInfo(cdtScoreList);
        return Result.success(pageInfo);

    }

    @GetMapping("buyscore.do")
    @IgnoreAuth
    public Result buyScore(@LoginUser UserVo loginUser, String money, String score) {
        if (loginUser == null) {
            return Result.failure("请先登录");
        }
        CdtScoreFlow cdtScoreFlow = new CdtScoreFlow();

        cdtScoreFlow.setFlowSn(CommonUtil.generateOrderNumber());
        cdtScoreFlow.setScore(new BigDecimal(score));
        cdtScoreFlow.setUserId(loginUser.getUserId());
        try {
            Map chain = BeanJwtUtil.javabean2map(cdtScoreFlow);
            String token = JavaWebToken.createJavaWebToken(chain);
            cdtScoreFlow.setToken(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //cdtScoreFlow.setMoney(new BigDecimal(money));
        cdtScoreFlow.setMoney(new BigDecimal(0.01));
        cdtScoreFlow.setCreateTime(new Date());
        boolean result = cdtScoreFlowService.save(cdtScoreFlow);
        if (result) {
            return Result.success(cdtScoreFlow);
        }
        return Result.failure("积分充值订单创建失败");
    }

    @GetMapping("myScore.json")
    @IgnoreAuth
    public Result myScore(@LoginUser UserVo loginUser) {
        if (loginUser == null) {
            return Result.failure("请先登录");
        }

        CdtScore score = cdtScoreService.getById(loginUser.getUserId());
        return Result.success(score);
    }

}
