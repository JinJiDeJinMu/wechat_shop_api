package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.annotation.LoginUser;
import com.chundengtai.base.bean.CdtDistributionLevel;
import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.bean.CdtRebateLog;
import com.chundengtai.base.bean.CdtUserSummary;
import com.chundengtai.base.bean.dto.CdtDistridetailDto;
import com.chundengtai.base.bean.dto.CdtRebateLogDto;
import com.chundengtai.base.entity.UserVo;
import com.chundengtai.base.result.R;
import com.chundengtai.base.service.*;
import com.chundengtai.base.weixinapi.DistributionStatus;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "v2分销中心管理")
@RestController
@RequestMapping("/apis/v2/distribution")
@Slf4j
public class WxDistributionController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CdtUserSummaryService cdtUserSummaryService;

    //用户层级服务
    @Autowired
    private CdtDistributionLevelService distributionLevelService;

    //用户服务类
    @Autowired
    private UserService userService;

    //订单服务类
    @Autowired
    private OrderService orderService;

    //用户购买分销得钱记录
    @Autowired
    private CdtDistridetailService distridetailService;

    //分销记录表
    @Autowired
    private CdtRebateLogService rebateLogService;

    @Autowired
    private MapperFacade mapperFacade;

    @ApiOperation(value = "分销中心个人发展信息汇总", httpMethod = "GET")
    @GetMapping("/getUserDistributionInfo.json")
    @ResponseBody
    @IgnoreAuth
    public R getUserDistributionInfo(@LoginUser UserVo loginUser) {

        CdtUserSummary userSummery = cdtUserSummaryService.getOne(new LambdaQueryWrapper<CdtUserSummary>()
                .eq(CdtUserSummary::getUserId, loginUser.getUserId()));

        BigDecimal unsetMoney = BigDecimal.ZERO;
        BigDecimal totalMoney = BigDecimal.ZERO;
        LambdaQueryWrapper<CdtDistridetail> conditionOne = new QueryWrapper<CdtDistridetail>().lambda()
                .eq(CdtDistridetail::getGoldUserId, loginUser.getUserId().intValue())
                .eq(CdtDistridetail::getStatus, DistributionStatus.COMPLETED_ORDER.getCode());
        unsetMoney = distridetailService.getUnsetMoney(conditionOne);

        LambdaQueryWrapper<CdtDistridetail> conditionTwo = new QueryWrapper<CdtDistridetail>().lambda()
                .eq(CdtDistridetail::getGoldUserId, loginUser.getUserId().intValue())
                .ne(CdtDistridetail::getStatus, DistributionStatus.REFUND_ORDER.getCode());
        totalMoney = distridetailService.getTotalMoney(conditionTwo);

        return R.ok().put("userSummery", userSummery).put("unsetMoney", unsetMoney).put("totalMoney", totalMoney);
    }

    @ApiOperation(value = "分销中心个人下线人数列表", httpMethod = "GET")
    @GetMapping("/getUserGroupList.json")
    @ResponseBody
    @IgnoreAuth
    public R getUserGroupList(Integer userId, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<CdtDistributionLevel> resultList = distributionLevelService.list(new QueryWrapper<CdtDistributionLevel>().lambda()
                .eq(CdtDistributionLevel::getUserId, userId));
        PageInfo pageInfo = new PageInfo(resultList);
        return R.ok().put("data", pageInfo);
    }

    @ApiOperation(value = "分销中心个人收益列表", httpMethod = "GET")
    @GetMapping("/getUserEarnings.json")
    @ResponseBody
    public R getUserInfoarnings(@LoginUser UserVo loginUser, Integer pageIndex, Integer pageSize) {

        LambdaQueryWrapper<CdtDistridetail> condition = new QueryWrapper<CdtDistridetail>().lambda()
                .eq(CdtDistridetail::getGoldUserId, loginUser.getUserId().intValue());

        PageHelper.startPage(pageIndex, pageSize);
        List<CdtDistridetail> resultList = distridetailService.list(
                condition);
        resultList = resultList.stream().sorted(Comparator.comparing(CdtDistridetail::getId).reversed()).collect(Collectors.toList());
        if (resultList.size() == 0) return R.error("已经是最后一页");
        List<CdtDistridetailDto> dtoList = mapperFacade.mapAsList(resultList, CdtDistridetailDto.class);
        PageInfo pageInfo = new PageInfo(dtoList);
        BigDecimal unsetMoney = BigDecimal.ZERO;
        BigDecimal totalMoney = BigDecimal.ZERO;
        if (pageInfo.getTotal() > 0) {
            LambdaQueryWrapper<CdtDistridetail> conditionOne = condition;
            conditionOne.eq(CdtDistridetail::getStatus, DistributionStatus.NON_COMPLETE_ORDER.getCode()).or()
                    .eq(CdtDistridetail::getStatus, DistributionStatus.NOT_SERVEN_ORDER.getCode());
            unsetMoney = distridetailService.getUnsetMoney(conditionOne);

            LambdaQueryWrapper<CdtDistridetail> conditionTwo = new QueryWrapper<CdtDistridetail>().lambda()
                    .eq(CdtDistridetail::getStatus, DistributionStatus.COMPLETED_ORDER.getCode())
                    .eq(CdtDistridetail::getGoldUserId, loginUser.getUserId().intValue());
            totalMoney = distridetailService.getTotalMoney(conditionTwo);
        }
        return R.ok().put("data", pageInfo).put("unsetMoney", unsetMoney).put("totalMoney", totalMoney);
    }

    @ApiOperation(value = "分销中心个人分享的订单列表", httpMethod = "GET")
    @GetMapping("/getUserShareOrder.json")
    @ResponseBody
    @IgnoreAuth
    public R getUserShareOrder(@LoginUser UserVo loginUser, Integer pageIndex,
                               Integer status,
                               Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        LambdaQueryWrapper<CdtRebateLog> condition = new QueryWrapper<CdtRebateLog>().lambda()
                .eq(CdtRebateLog::getGoldUserId, loginUser.getUserId().intValue());
        if (status != null && status != 0) {
            condition.eq(CdtRebateLog::getStatus, status);
        }
        List<CdtRebateLog> resultList = rebateLogService.list(condition);
        resultList = resultList.stream().sorted(Comparator.comparing(CdtRebateLog::getId).reversed()).collect(Collectors.toList());
        List<CdtRebateLogDto> result = mapperFacade.mapAsList(resultList, CdtRebateLogDto.class);
        PageInfo pageInfo = new PageInfo(result);
        return R.ok().put("data", pageInfo);
    }
}
