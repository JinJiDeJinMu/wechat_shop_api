package com.platform.controller;
import com.platform.entity.CdtCustomerServiceEntity;
import com.platform.service.CdtCustomerServiceService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 客服管理表Controller
 *
 * @author lipengjun
 * @date 2019-11-25 19:27:34
 */
@RestController
@RequestMapping("cdtcustomerservice")
public class CdtCustomerServiceController {
    @Autowired
    private CdtCustomerServiceService cdtCustomerServiceService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("cdtcustomerservice:list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<CdtCustomerServiceEntity> cdtCustomerServiceList = cdtCustomerServiceService.queryList(query);
        int total = cdtCustomerServiceService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(cdtCustomerServiceList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("cdtcustomerservice:info")
    @ResponseBody
    public R info(@PathVariable("id") Integer id) {
        CdtCustomerServiceEntity cdtCustomerService = cdtCustomerServiceService.queryObject(id);

        return R.ok().put("cdtCustomerService", cdtCustomerService);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("cdtcustomerservice:save")
    @ResponseBody
    public R save(@RequestBody CdtCustomerServiceEntity cdtCustomerService) {
        cdtCustomerServiceService.save(cdtCustomerService);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("cdtcustomerservice:update")
    @ResponseBody
    public R update(@RequestBody CdtCustomerServiceEntity cdtCustomerService) {
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

        List<CdtCustomerServiceEntity> list = cdtCustomerServiceService.queryList(params);

        return R.ok().put("list", list);
    }
}
