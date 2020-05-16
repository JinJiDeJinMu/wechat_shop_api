package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chundengtai.base.annotation.LoginUser;
import com.chundengtai.base.bean.CdtScoreFlow;
import com.chundengtai.base.bean.CdtUscore;
import com.chundengtai.base.bean.CdtUserScore;
import com.chundengtai.base.entity.UserVo;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.CdtScoreFlowService;
import com.chundengtai.base.service.CdtUscoreService;
import com.chundengtai.base.service.CdtUserScoreService;
import com.chundengtai.base.util.ApiBaseAction;
import com.chundengtai.base.util.CommonUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
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
    private CdtScoreFlowService cdtScoreFlowService;

    @Autowired
    private CdtUserScoreService cdtUserScoreService;

    @Autowired
    private CdtUscoreService cdtUscoreService;

    @GetMapping("buyscore.do")
    public Result buyScore(@LoginUser UserVo loginUser, String money, String score) {
        CdtScoreFlow cdtScoreFlow = new CdtScoreFlow();

        cdtScoreFlow.setFlowSn(CommonUtil.generateOrderNumber());
        cdtScoreFlow.setScore(new BigDecimal(score));
        cdtScoreFlow.setUserId(loginUser.getUserId());

        cdtScoreFlow.setMoney(new BigDecimal(money));

        cdtScoreFlow.setCreateTime(new Date());
        boolean result = cdtScoreFlowService.save(cdtScoreFlow);
        if (result) {
            return Result.success(cdtScoreFlow);
        }
        return Result.failure("充值订单创建失败");
    }

    @GetMapping("myScore.json")
    public Result myScore(@LoginUser UserVo loginUser) {

        CdtUserScore cdtUserScore = cdtUserScoreService.getById(loginUser.getUserId());
        Map<String,Object> hashmap = new HashMap<>();
        hashmap.put("flag",false);
        hashmap.put("cdtUserScore",cdtUserScore);
        return Result.success(hashmap);
    }

    /**
     * 积分流水
     * @param loginUser
     * @return
     */
    @GetMapping("uScore.json")
    public Result uScore(@LoginUser UserVo loginUser) {

        List<CdtUscore> uscoreList = cdtUscoreService.list(new LambdaQueryWrapper<CdtUscore>()
                .eq(CdtUscore::getUserId,loginUser.getUserId()));
        return Result.success(uscoreList);
    }

}
