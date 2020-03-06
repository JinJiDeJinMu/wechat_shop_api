package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CdtPostageTemplate;
import com.chundengtai.base.service.CdtPostageTemplateService;
import com.chundengtai.base.transfer.BaseForm;
import com.chundengtai.base.utils.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cdtPostageTemplate")
public class CdtPostageTemplateController {
@Autowired
public CdtPostageTemplateService cdtPostageTemplateService;

/**
 * 列表
 */
@PostMapping("/list.json")
public R list(@RequestBody BaseForm<CdtPostageTemplate> params){
        QueryWrapper<CdtPostageTemplate> conditon=new QueryWrapper<>();
        if(!StringUtils.isEmpty(params.getSortField())&&!StringUtils.isEmpty(params.getOrder())){
        if(params.getOrder().equalsIgnoreCase("asc")){
        conditon.orderByAsc(params.getSortField());
        }
        else{
        conditon.orderByDesc(params.getSortField());
        }
        }
                //Integer=====
                                if(params.getData().getId()!=null){
                conditon.eq("id",params.getData().getId());
                }
                        //String=====
                                if(params.getData().getTopic()!=null){
                conditon.eq("topic",params.getData().getTopic());
                }
                        //String=====
                                if(params.getData().getExpressCompany()!=null){
                conditon.eq("express_company",params.getData().getExpressCompany());
                }
                        //Integer=====
                                if(params.getData().getPostageType()!=null){
                conditon.eq("postage_type",params.getData().getPostageType());
                }
                        //Integer=====
                                if(params.getData().getChargingType()!=null){
                conditon.eq("charging_type",params.getData().getChargingType());
                }
                        //String=====
                                if(params.getData().getAddress()!=null){
                conditon.eq("address",params.getData().getAddress());
                }
                        //Integer=====
                                if(params.getData().getFirst()!=null){
                conditon.eq("first",params.getData().getFirst());
                }
                        //BigDecimal=====
                                if(params.getData().getMoney()!=null){
                conditon.eq("money",params.getData().getMoney());
                }
                        //Integer=====
                                if(params.getData().getSecond()!=null){
                conditon.eq("second",params.getData().getSecond());
                }
                        //BigDecimal=====
                                if(params.getData().getRenewMoney()!=null){
                conditon.eq("renew_money",params.getData().getRenewMoney());
                }
                        //Date=====
                                //Date=====
                                //String=====
                                if(params.getData().getRemark()!=null){
                conditon.eq("remark",params.getData().getRemark());
                }
                        //String=====
                                if(params.getData().getCreater()!=null){
                conditon.eq("creater",params.getData().getCreater());
                }
                    PageHelper.startPage(params.getPageIndex(),params.getPageSize());
        List<CdtPostageTemplate> collectList=cdtPostageTemplateService.list(conditon);
        PageInfo pageInfo=new PageInfo(collectList);
        return R.ok(pageInfo);
        }

/**
 * 信息
 */
@GetMapping("/getModel/{id}.json")
public R info(@PathVariable("id") Integer id){
    CdtPostageTemplate model= cdtPostageTemplateService.getById(id);
        return R.ok(model);
        }

/**
 * 保存
 */
@PostMapping("/saveModel.json")
public R save(@RequestBody CdtPostageTemplate paramModel){
        boolean result= cdtPostageTemplateService.save(paramModel);
        return R.ok(result);
        }

/**
 * 修改
 */
@PostMapping("/updateModel.json")
public R update(@RequestBody CdtPostageTemplate paramModel){
        boolean result= cdtPostageTemplateService.updateById(paramModel);
        return R.ok(result);
        }

/**
 * 删除
 */
@PostMapping("/deleteModel.json")
public R delete(@RequestBody Integer[]ids){
        boolean result= cdtPostageTemplateService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
        }

    @GetMapping("/queryAll")
    public R getAll() {
        return R.ok(cdtPostageTemplateService.list());
    }
}
