package com.platform.controller;


import com.platform.entity.CdtPaytransRecordEntity;
import com.platform.service.CdtPaytransRecordService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 系统流水日志Controller
 *
 * @author lipengjun
 * @date 2019-12-12 17:45:18
 */
@Controller
@RequestMapping("cdtpaytransrecord")
public class CdtPaytransRecordController {
    @Autowired
    private CdtPaytransRecordService cdtPaytransRecordService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) throws ParseException {
        //查询列表数据
        Query query = new Query(params);
        List<CdtPaytransRecordEntity> cdtPaytransRecordList = cdtPaytransRecordService.queryList(query);
        int total = cdtPaytransRecordService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(cdtPaytransRecordList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("cdtpaytransrecord:info")
    @ResponseBody
    public R info(@PathVariable("id") Long id) {
        CdtPaytransRecordEntity cdtPaytransRecord = cdtPaytransRecordService.queryObject(id);

        return R.ok().put("cdtPaytransRecord", cdtPaytransRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("cdtpaytransrecord:save")
    @ResponseBody
    public R save(@RequestBody CdtPaytransRecordEntity cdtPaytransRecord) {
        cdtPaytransRecordService.save(cdtPaytransRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("cdtpaytransrecord:update")
    @ResponseBody
    public R update(@RequestBody CdtPaytransRecordEntity cdtPaytransRecord) {
        cdtPaytransRecordService.update(cdtPaytransRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("cdtpaytransrecord:delete")
    @ResponseBody
    public R delete(@RequestBody Long[] ids) {
        cdtPaytransRecordService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<CdtPaytransRecordEntity> list = cdtPaytransRecordService.queryList(params);

        return R.ok().put("list", list);
    }
}
