package com.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
                QueryWrapper<CommentPicture> conditon = new QueryWrapper<>();
                if (!StringUtils.isEmpty(params.getSortField()) && !StringUtils.isEmpty(params.getOrder())) {
                        if (params.getOrder().equalsIgnoreCase("asc")) {
                                conditon.orderByAsc(params.getSortField());
                        } else {
                                conditon.orderByDesc(params.getSortField());
                        }
                }
            if (params.getData().getStatus() != null) {
                conditon.eq("status", params.getData().getStatus());
            }
            if (params.getData().getCommentId() != null) {
                conditon.eq("comment_id", params.getData().getCommentId());
            }
            if (params.getData().getType() != null) {
                conditon.eq("type", params.getData().getType());
            }
                PageHelper.startPage(params.getPageIndex(), params.getPageSize());
                List<CommentPicture> collectList = commentPictureService.list(conditon);
                PageInfo pageInfo = new PageInfo(collectList);
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
            System.out.println(paramModel.getStatus());
            boolean result = commentPictureService.update(new LambdaUpdateWrapper<CommentPicture>()
                    .set(CommentPicture::getStatus, paramModel.getStatus()).eq(CommentPicture::getId, paramModel.getId()));
                return R.ok(result);
        }

        /**
         * 删除
         */
        @PostMapping("/deleteModel.json")
//@RequiresPermissions("commentpicture:deleteModel")
        public R delete(@RequestBody Integer[] ids) {

            for (Integer id : ids
                    ) {
                System.out.println("==" + id);
                commentPictureService.update(new LambdaUpdateWrapper<CommentPicture>()
                        .set(CommentPicture::getStatus, 1)
                        .eq(CommentPicture::getId, id));
            }
            return R.ok();
        }
}
