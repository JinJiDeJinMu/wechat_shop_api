package com.chundengtai.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chundengtai.base.bean.CdtScoreFlow;
import com.chundengtai.base.bean.CdtUserScore;
import com.chundengtai.base.jwt.JavaWebToken;
import com.chundengtai.base.service.CdtScoreFlowService;
import com.chundengtai.base.service.CdtUserScoreService;
import com.chundengtai.base.service.UserScoreService;
import com.chundengtai.base.utils.BeanJwtUtil;
import com.chundengtai.base.weixinapi.PayTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Description TODO
 * @Author hujinbiao
 * @Date 2020年1月18日 0018 上午 11:00:25
 * @Version 1.0
 **/
@Service
@Slf4j
public class UserScoreServiceImp implements UserScoreService {

    @Autowired
    private CdtScoreFlowService cdtScoreFlowService;

    @Autowired
    private CdtUserScoreService cdtUserScoreService;

    @Override
    public void addUserScore(String ScoreFlowSn) {
        CdtScoreFlow cdtScoreFlow = cdtScoreFlowService.getOne(new LambdaQueryWrapper<CdtScoreFlow>()
                .eq(CdtScoreFlow::getFlowSn, ScoreFlowSn));

        if (cdtScoreFlow != null) {
            cdtScoreFlow.setPayTime(new Date());
            cdtScoreFlow.setPayStatus(PayTypeEnum.PAYED.getCode());
            boolean result = cdtScoreFlowService.updateById(cdtScoreFlow);
            //更新用户积分信息
            if (result) {
                long userId = cdtScoreFlow.getUserId();
                CdtUserScore cdtUserScore = cdtUserScoreService.getById(userId);
                if (cdtUserScore == null) {
                    CdtUserScore userScore = new CdtUserScore();
                    userScore.setId(userId);
                    userScore.setScore(cdtScoreFlow.getScore());
                    userScore.setTotalScore(cdtScoreFlow.getScore());
                    //计算等级
                    userScore.setLevel(changeUserLevel(cdtScoreFlow.getScore()));
                    userScore.setToken(gettoken(userScore));
                    userScore.setCreateTime(new Date());
                    log.info("userScore==="+userScore +"=id="+userScore.getId());
                    cdtUserScoreService.save(userScore);
                } else {
                    //存在更新积分数
                    String token = cdtUserScore.getToken();
                    cdtUserScore.setCreateTime(null);
                    cdtUserScore.setToken(null);

                    String token_flag = gettoken(cdtUserScore);
                    if (token.equalsIgnoreCase(token_flag)) {
                        BigDecimal score = cdtUserScore.getScore().add(cdtScoreFlow.getScore());
                        BigDecimal totalScore = cdtUserScore.getTotalScore().add(cdtScoreFlow.getScore());
                        cdtUserScore.setScore(score);
                        cdtUserScore.setTotalScore(totalScore);
                        cdtUserScore.setToken(gettoken(cdtUserScore));
                        cdtUserScore.setCreateTime(new Date());
                        cdtUserScore.setId(userId);
                        cdtUserScore.setLevel(changeUserLevel(totalScore));
                        cdtUserScoreService.updateById(cdtUserScore);
                    }
                }
            }
        }

    }
    /**
     * 判断等级
     *
     * @param totalScore
     */
    public Integer changeUserLevel(BigDecimal totalScore) {
        Integer score = totalScore.intValue();

        if (score > 0 && score <= 10000) {
            return 1;
        } else if (score > 10000 && score <= 200000) {
            return 2;
        } else if (score > 200000 && score <= 500000) {
            return 3;
        } else if (score > 500000 && score <= 1000000) {
            return 4;
        } else if (score > 1000000 && score <= 2000000) {
            return 5;
        } else if (score > 2000000 && score <= 5000000) {
            return 6;
        } else if (score > 5000000 && score < 10000000) {
            return 7;
        } else {
            return 8;
        }
    }

    public String gettoken(Object object) {
        Map chain = null;
        try {
            chain = BeanJwtUtil.javabean2map(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String token = JavaWebToken.createJavaWebToken(chain);
        return token;
    }
}
