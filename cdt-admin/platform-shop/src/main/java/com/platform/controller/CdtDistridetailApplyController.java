package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtDistridetailApply;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.CdtDistridetailApplyService;
import com.chundengtai.base.transfer.BaseForm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cdtDistridetailApply")
public class CdtDistridetailApplyController {
@Autowired
public CdtDistridetailApplyService cdtDistridetailApplyService;

/**
 * 列表
 */
@PostMapping("/list.json")
@RequiresPermissions("cdtdistridetailapply:list")
public R list(@RequestBody BaseForm<CdtDistridetailApply> params){
        QueryWrapper<CdtDistridetailApply> conditon=new QueryWrapper<>();
        if(!StringUtils.isEmpty(params.getSortField())&&!StringUtils.isEmpty(params.getOrder())){
        if(params.getOrder().equalsIgnoreCase("asc")){
        conditon.orderByAsc(params.getSortField());
        }
        else{
        conditon.orderByDesc(params.getSortField());
        }
        }
                //Long=====
                                if(params.getData().getId()!=null){
                conditon.eq("id",params.getData().getId());
                }
                        //String=====
                                if(params.getData().getOrderSn()!=null){
                conditon.eq("order_sn",params.getData().getOrderSn());
                }
                        //Integer=====
                                if(params.getData().getStatus()!=null){
                conditon.eq("status",params.getData().getStatus());
                }
                        //BigDecimal=====
                                if(params.getData().getMoney()!=null){
                conditon.eq("money",params.getData().getMoney());
                }
                        //String=====
                                if(params.getData().getWeixinOpenid()!=null){
                conditon.eq("weixinOpenid",params.getData().getWeixinOpenid());
                }
                        //String=====
                                if(params.getData().getUserName()!=null){
                conditon.eq("userName",params.getData().getUserName());
                }
                        //String=====
                                if(params.getData().getRealName()!=null){
                conditon.eq("realName",params.getData().getRealName());
                }
                        //Date=====
                                //String=====
                                if(params.getData().getOperator()!=null){
                conditon.eq("operator",params.getData().getOperator());
                }
                        //Date=====
                                //Integer=====
                                if(params.getData().getType()!=null){
                conditon.eq("type",params.getData().getType());
                }
                    PageHelper.startPage(params.getPageIndex(),params.getPageSize());
        List<CdtDistridetailApply> collectList=cdtDistridetailApplyService.list(conditon);
        PageInfo pageInfo=new PageInfo(collectList);
        return R.ok(pageInfo);
        }

/**
 * 信息
 */
@GetMapping("/getModel/{id}.json")
@RequiresPermissions("cdtdistridetailapply:getModel")
public R info(@PathVariable("id") Integer id){
    CdtDistridetailApply model= cdtDistridetailApplyService.getById(id);
        return R.ok(model);
        }

/**
 * 保存
 */
@PostMapping("/saveModel.json")
@RequiresPermissions("cdtdistridetailapply:saveModel")
public R save(@RequestBody CdtDistridetailApply paramModel){
        boolean result= cdtDistridetailApplyService.save(paramModel);
        return R.ok(result);
        }

/**
 * 修改
 */
@PostMapping("/updateModel.json")
@RequiresPermissions("cdtdistridetailapply:updateModel")
public R update(@RequestBody CdtDistridetailApply paramModel){
        boolean result= cdtDistridetailApplyService.updateById(paramModel);
        return R.ok(result);
        }

/**
 * 删除
 */
@PostMapping("/deleteModel.json")
@RequiresPermissions("cdtdistridetailapply:deleteModel")
public R delete(@RequestBody Integer[]ids){
        boolean result= cdtDistridetailApplyService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
        }
        }
