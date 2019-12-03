package com.platform.api;

import com.chundengtai.base.result.Result;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.CdtCustomerServiceVo;
import com.platform.service.ApiCdtCustomerServiceService;
import com.platform.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 客服管理表Controller
 *
 * @author lipengjun
 * @date 2019-11-25 19:27:34
 */
@RestController
@RequestMapping("/api/v2/cdtcustomerservice")
public class ApiCdtCustomerServiceController {
    @Autowired
    private ApiCdtCustomerServiceService cdtCustomerServiceService;

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
}
