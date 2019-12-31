package com.chundengtai.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistributionLevel;
import com.chundengtai.base.bean.CdtDistrimoney;
import com.chundengtai.base.bean.CdtUserSummary;
import com.chundengtai.base.weixinapi.TrueOrFalseEnum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * **************************************************************
 * 公司名称    :
 * 系统名称    :未名严选
 * 类 名 称    :合伙人逻辑处理类
 * 功能描述    :
 * 业务描述    :
 * 作 者 名    :@Author Royal(方圆)
 * 开发日期    :2019/12/31 下午4:08
 * Created    :IntelliJ IDEA
 * **************************************************************
 * 修改日期    :
 * 修 改 者    :
 * 修改内容    :
 * **************************************************************
 */
@Service
@Slf4j
public class PartnerService {

    @Autowired
    private CdtUserSummaryService cdtUserSummaryService;

    //用户层级服务
    @Autowired
    private CdtDistributionLevelService distributionLevelService;

    /**
     * 轮询获取到可以拿合伙人奖励的合伙人
     */
    public CdtUserSummary getPartnerInfo(CdtDistrimoney distrimoney, int userId) {
        //读取链路关系
        CdtUserSummary cdtUserSummary = cdtUserSummaryService.getOne(new QueryWrapper<CdtUserSummary>().lambda()
                .eq(CdtUserSummary::getUserId, userId));

        if (cdtUserSummary == null) return null;
        Gson gson = new Gson();

        Type linkNodeType = new TypeToken<LinkedList<Integer>>() {
        }.getType();

        if (StringUtils.isEmpty(cdtUserSummary.getChainRoad())) {
            log.warn("======绑定结构层级数据有误请检查========");
            return null;
        }
        LinkedList<Integer> linkNode = gson.fromJson(cdtUserSummary.getChainRoad(), linkNodeType);

        LambdaQueryWrapper<CdtUserSummary> queryWrapper = new QueryWrapper<CdtUserSummary>().lambda()
                .in(CdtUserSummary::getUserId, linkNode);
        //.eq(CdtUserSummary::getIsPartner, TrueOrFalseEnum.TRUE.getCode())

        log.warn("=========>" + queryWrapper.getSqlSegment());
        List<CdtUserSummary> userSummarys = cdtUserSummaryService.list(
                queryWrapper
        );

        CdtUserSummary userSummary = null;

        long exsit = userSummarys.stream().filter(s -> s.getIsPartner().equals(TrueOrFalseEnum.TRUE.getCode())).count();
        //如果合伙人数量为0那么直接返回
        if (exsit == 0) {
            return userSummary;
        }

        LinkedList<Integer> linkNodeTemp = linkNode;

        while (linkNodeTemp.size() != 0) {
            Integer roadUserId = linkNodeTemp.pollLast();
            System.out.println(roadUserId);
            // 相当于倒叙输出；
            Optional<CdtUserSummary> itemOptional = userSummarys.stream().filter(s -> s.getUserId().equals(roadUserId)).findFirst();
            if (!itemOptional.isPresent()) {
                continue;
            }

            userSummary = itemOptional.get();
            if (!userSummary.getIsPartner().equals(TrueOrFalseEnum.TRUE.getCode())) {
                continue;
            }

            int index = linkNode.indexOf(roadUserId);
            log.info("linkNode--index:==>" + index);
            log.info("linkNode--size:==>" + linkNode.size());

            try {
                //判定是合伙人的哪一级
                CdtDistributionLevel levelModel = distributionLevelService.getOne(new QueryWrapper<CdtDistributionLevel>()
                        .lambda().eq(CdtDistributionLevel::getUserId, linkNode.get(index + 1)));

                //用户所在的层级是在合伙人界限内还是外
                if (levelModel.getDevNum() <= distrimoney.getFirstPersonCondition()) {
                    continue;
                }
                //用户排在第21及之后的话那么合伙人奖励直接给到当前的这个合伙人
                else {
                    return userSummary;
                }
            } catch (Exception ex) {
                log.error("========轮询合伙人异常对应的参数为=====index--->" + index);
                log.error("========轮询合伙人异常对应的参数为=====linkeNode.size--->" + linkNode.size());
                ex.printStackTrace();
            }
        }
        return userSummary;

    }
}
