package com.chundengtai.base.controller;

import com.chundengtai.base.bean.OrderApply;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.OrderApplyService;
import com.chundengtai.base.transfer.BaseForm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.chundengtai.base.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderApply")
public class OrderApplyController {
@Autowired
public OrderApplyService orderApplyService;

/**
 * 列表
 */
@PostMapping("/list.json")
@RequiresPermissions("orderapply:list")
public R list(@RequestBody BaseForm<OrderApply> params){
        QueryWrapper<OrderApply> conditon=new QueryWrapper<>();
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
                                if(params.getData().getOrderSn()!=null){
                conditon.eq("order_sn",params.getData().getOrderSn());
                }
                        //Long=====
                                if(params.getData().getMerchantId()!=null){
                conditon.eq("merchant_id",params.getData().getMerchantId());
                }
                        //String=====
                                if(params.getData().getMerchantName()!=null){
                conditon.eq("merchant_name",params.getData().getMerchantName());
                }
                        //BigDecimal=====
                                if(params.getData().getMoney()!=null){
                conditon.eq("money",params.getData().getMoney());
                }
                        //Long=====
                                if(params.getData().getUserId()!=null){
                conditon.eq("user_id",params.getData().getUserId());
                }
                        //String=====
                                if(params.getData().getWxopenId()!=null){
                conditon.eq("wxopen_id",params.getData().getWxopenId());
                }
                        //String=====
                                if(params.getData().getUserName()!=null){
                conditon.eq("user_name",params.getData().getUserName());
                }
                        //Date=====
                                //Date=====
                                //Integer=====
                                if(params.getData().getStatus()!=null){
                conditon.eq("status",params.getData().getStatus());
                }
                        //String=====
                                if(params.getData().getRemarks()!=null){
                conditon.eq("remarks",params.getData().getRemarks());
                }
                    PageHelper.startPage(params.getPageIndex(),params.getPageSize());
        List<OrderApply> collectList=orderApplyService.list(conditon);
        PageInfo pageInfo=new PageInfo(collectList);
        return R.ok(pageInfo);
        }

/**
 * 信息
 */
@GetMapping("/getModel/{id}.json")
@RequiresPermissions("orderapply:getModel")
public R info(@PathVariable("id") Integer id){
    OrderApply model= orderApplyService.getById(id);
        return R.ok(model);
        }

/**
 * 保存
 */
@PostMapping("/saveModel.json")
@RequiresPermissions("orderapply:saveModel")
public R save(@RequestBody OrderApply paramModel){
        boolean result= orderApplyService.save(paramModel);
        return R.ok(result);
        }

/**
 * 修改
 */
@PostMapping("/updateModel.json")
@RequiresPermissions("orderapply:updateModel")
public R update(@RequestBody OrderApply paramModel){
        boolean result= orderApplyService.updateById(paramModel);
        return R.ok(result);
        }

/**
 * 删除
 */
@PostMapping("/deleteModel.json")
@RequiresPermissions("orderapply:deleteModel")
public R delete(@RequestBody Integer[]ids){
        boolean result= orderApplyService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
        }
        }
