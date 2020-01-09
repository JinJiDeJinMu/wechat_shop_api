package com.platform.controller;

import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import com.chundengtai.base.utils.ShiroUtils;
import com.platform.entity.FeedbackEntity;
import com.platform.service.FeedbackService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @date 2017-08-23 15:03:25
 */
@RestController
@RequestMapping("feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("feedback:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        query.put("merchant_id", ShiroUtils.getUserEntity().getMerchantId());
        List<FeedbackEntity> feedbackList = feedbackService.queryList(query);
        int total = feedbackService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(feedbackList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{msgId}")
    @RequiresPermissions("feedback:info")
    public R info(@PathVariable("msgId") Integer msgId) {
        FeedbackEntity feedback = feedbackService.queryObject(msgId);

        return R.ok().put("feedback", feedback);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("feedback:save")
    public R save(@RequestBody FeedbackEntity feedback) {
        feedback.setMerchant_id(ShiroUtils.getUserEntity().getMerchantId().intValue());
        feedbackService.save(feedback);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("feedback:update")
    public R update(@RequestBody FeedbackEntity feedback) {
        feedbackService.update(feedback);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("feedback:delete")
    public R delete(@RequestBody Integer[] msgIds) {
        feedbackService.deleteBatch(msgIds);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<FeedbackEntity> list = feedbackService.queryList(params);

        return R.ok().put("list", list);
    }
}
