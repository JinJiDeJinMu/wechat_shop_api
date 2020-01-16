package com.chundengtai.base.controller;

import com.chundengtai.base.entity.CommentPictureEntity;
import com.chundengtai.base.entity.CommentV2Entity;
import com.chundengtai.base.service.admin.CommentPictureService;
import com.chundengtai.base.service.admin.CommentV2Service;
import com.chundengtai.base.utils.PageUtils;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 评价表Controller
 */
@RestController
@RequestMapping("commentv2")
public class CommentV2Controller {
    @Autowired
    private CommentV2Service CommentV2Service;
    @Autowired
    private CommentPictureService commentPictureService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("CommentV2:list")
    @ResponseBody
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<CommentV2Entity> CommentV2List = CommentV2Service.queryList(query);
        int total = CommentV2Service.queryTotal(query);
        for (CommentV2Entity commentV2Entity : CommentV2List) {
            Map paramPicture = new HashMap();
            paramPicture.put("commentId", commentV2Entity.getId());
            List<CommentPictureEntity> commentPictureEntities = commentPictureService.queryList(paramPicture);
            commentV2Entity.setCommentPictureEntities(commentPictureEntities);
        }
        PageUtils pageUtil = new PageUtils(CommentV2List, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("CommentV2:info")
    @ResponseBody
    public R info(@PathVariable("id") Integer id) {
        CommentV2Entity CommentV2 = CommentV2Service.queryObject(id);

        return R.ok().put("CommentV2", CommentV2);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("CommentV2:save")
    @ResponseBody
    public R save(@RequestBody CommentV2Entity CommentV2) {
        CommentV2Service.save(CommentV2);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("CommentV2:update")
    @ResponseBody
    public R update(@RequestBody CommentV2Entity CommentV2) {
        CommentV2Service.update(CommentV2);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("CommentV2:delete")
    @ResponseBody
    public R delete(@RequestBody Integer[] ids) {
        CommentV2Service.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    @ResponseBody
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<CommentV2Entity> list = CommentV2Service.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 修改状态
     */
    @RequestMapping("/toggleStatus")
    @ResponseBody
    public R toggleStatus(@RequestBody CommentV2Entity comment) {
        CommentV2Service.toggleStatus(comment);

        return R.ok();
    }
}
