package com.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.CommentPicture;
import com.chundengtai.base.service.CommentPictureService;
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
@RequestMapping("/commentPicture")
public class CommentPictureController {
        @Autowired
        public CommentPictureService commentPictureService;

        /**
         * 列表
         */
        @PostMapping("/list.json")
//@RequiresPermissions("commentpicture:list")
        public R list(@RequestBody BaseForm<CommentPicture> params) {
                System.out.println("++++" + params);
                QueryWrapper<CommentPicture> conditon = new QueryWrapper<>();
                if (!StringUtils.isEmpty(params.getSortField()) && !StringUtils.isEmpty(params.getOrder())) {
                        if (params.getOrder().equalsIgnoreCase("asc")) {
                                conditon.orderByAsc(params.getSortField());
                        } else {
                                conditon.orderByDesc(params.getSortField());
                        }
                }

                PageHelper.startPage(params.getPageIndex(), params.getPageSize());
                List<CommentPicture> collectList = commentPictureService.list(conditon);
                PageInfo pageInfo = new PageInfo(collectList);
                System.out.println("===" + pageInfo);
                return R.ok(pageInfo);
        }

        /**
         * 信息
         */
        @GetMapping("/getModel/{id}.json")
//@RequiresPermissions("commentpicture:getModel")
        public R info(@PathVariable("id") Integer id) {
                CommentPicture model = commentPictureService.getById(id);
                return R.ok(model);
        }

        /**
         * 保存
         */
        @PostMapping("/saveModel.json")
        @RequiresPermissions("commentpicture:saveModel")
        public R save(@RequestBody CommentPicture paramModel) {
                boolean result = commentPictureService.save(paramModel);
                return R.ok(result);
        }

        /**
         * 修改
         */
        @PostMapping("/updateModel.json")
//@RequiresPermissions("commentpicture:updateModel")
        public R update(@RequestBody CommentPicture paramModel) {
                boolean result = commentPictureService.updateById(paramModel);
                return R.ok(result);
        }

        /**
         * 删除
         */
        @PostMapping("/deleteModel.json")
//@RequiresPermissions("commentpicture:deleteModel")
        public R delete(@RequestBody Integer[] ids) {
                boolean result = commentPictureService.removeByIds(Arrays.asList(ids));
                return R.ok(result);
        }
}
