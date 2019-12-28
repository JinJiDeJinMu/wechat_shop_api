package com.platform.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistributionLevel;
import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.bean.CdtRebateLog;
import com.chundengtai.base.bean.CdtUserSummary;
import com.chundengtai.base.bean.dto.CdtDistridetailDto;
import com.chundengtai.base.bean.dto.CdtRebateLogDto;
import com.chundengtai.base.bean.dto.CdtUserSummaryDto;
import com.chundengtai.base.service.*;
import com.chundengtai.base.weixinapi.DistributionStatus;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.entity.UserVo;
import com.platform.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Api(tags = "v2分销中心管理")
@RestController
@RequestMapping("/apis/v2/distribution")
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
        CdtUserSummary model = cdtUserSummaryService.getOne(new QueryWrapper<CdtUserSummary>().lambda().eq(CdtUserSummary::getUserId, loginUser.getUserId().intValue()));
        CdtUserSummaryDto result = mapperFacade.map(model, CdtUserSummaryDto.class);
        return R.ok().put("data", result);
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

        List<CdtRebateLogDto> result = mapperFacade.mapAsList(resultList, CdtRebateLogDto.class);
        PageInfo pageInfo = new PageInfo(result);
        return R.ok().put("data", pageInfo);
    }
}
