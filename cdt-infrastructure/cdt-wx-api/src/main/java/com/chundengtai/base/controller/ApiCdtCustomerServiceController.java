package com.chundengtai.base.controller;

import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.entity.CdtCustomerServiceVo;
import com.chundengtai.base.entity.CdtMerchantEntity;
import com.chundengtai.base.entity.GoodsVo;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.ApiCdtCustomerServiceService;
import com.chundengtai.base.service.ApiGoodsService;
import com.chundengtai.base.service.CdtMerchantWxService;
import com.chundengtai.base.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 客服管理表Controller
 */
@RestController
@RequestMapping("/api/v2/cdtcustomerservice")
public class ApiCdtCustomerServiceController {
    @Autowired
    private ApiCdtCustomerServiceService cdtCustomerServiceService;

    @Autowired
    private CdtMerchantWxService cdtMerchantService;

    @Autowired
    private ApiGoodsService apiGoodsService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("cdtcustomerservice:list")
    @IgnoreAuth
    @ResponseBody
    public Result<Map<String, Object>> list() {
        //查询列表数据
        Map<String, Object> params = new HashMap<>();
        List<CdtCustomerServiceVo> cdtCustomerServiceList = cdtCustomerServiceService.queryList(params);
        int total = cdtCustomerServiceService.queryTotal(params);
        Map<String, Object> cdtCustomerServiceLists = new HashMap<String, Object>();
        cdtCustomerServiceLists.put("cdtCustomerServiceList", cdtCustomerServiceList);
        return Result.success(cdtCustomerServiceLists);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("cdtcustomerservice:info")
    @IgnoreAuth
    @ResponseBody
    public R info(@PathVariable("id") Integer id) {
        CdtCustomerServiceVo cdtCustomerService = cdtCustomerServiceService.queryObject(id);
        return R.ok().put("cdtCustomerService", cdtCustomerService);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("cdtcustomerservice:save")
    @IgnoreAuth
    @ResponseBody
    public R save(@RequestBody CdtCustomerServiceVo cdtCustomerService) {
        cdtCustomerServiceService.save(cdtCustomerService);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("cdtcustomerservice:update")
    @IgnoreAuth
    @ResponseBody
    public R update(@RequestBody CdtCustomerServiceVo cdtCustomerService) {
        cdtCustomerServiceService.update(cdtCustomerService);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("cdtcustomerservice:delete")
    @ResponseBody
    public R delete(@RequestBody Integer[] ids) {
        cdtCustomerServiceService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<CdtCustomerServiceVo> list = cdtCustomerServiceService.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 查询商户信息
     *
     * @param goodsId
     * @return
     */
    @RequestMapping("/cdtMechant/{goodsId}")
    @ResponseBody
    public R queryList(@PathVariable("goodsId") Integer goodsId) {

        GoodsVo goodsVo = apiGoodsService.queryObject(goodsId);
        CdtMerchantEntity cdtMerchantEntity = new CdtMerchantEntity();
        if (goodsVo.getMerchantId() > 0) {//判断商品是不是管理员添加
            cdtMerchantEntity = cdtMerchantService.queryByGoodsId(goodsId);
        }
        System.out.println("cdt------" + cdtMerchantEntity);
        return R.ok().put("merchant", cdtMerchantEntity);
    }
}
