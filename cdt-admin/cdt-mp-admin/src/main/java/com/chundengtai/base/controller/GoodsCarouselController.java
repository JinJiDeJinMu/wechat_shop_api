package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.bean.GoodsCarousel;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.GoodsCarouselService;
import com.chundengtai.base.transfer.BaseForm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.chundengtai.base.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/goodsCarousel")
public class GoodsCarouselController {
@Autowired
public GoodsCarouselService goodsCarouselService;

/**
 * 列表
 */
@RequestMapping("/list.json")
public R list(@RequestParam Integer goodsId){
       List<GoodsCarousel> goodsCarouselList = goodsCarouselService.list(new LambdaQueryWrapper<GoodsCarousel>()
       .eq(GoodsCarousel::getGoodsId,goodsId));
        return R.ok().put("list",goodsCarouselList);
        }

/**
 * 信息
 */
@GetMapping("/getModel/{id}.json")
public R info(@PathVariable("id") Integer id){
    GoodsCarousel model= goodsCarouselService.getById(id);
        return R.ok(model);
        }

/**
 * 保存
 */
@PostMapping("/saveModel.json")
public R save(@RequestBody GoodsCarousel paramModel){
        boolean result= goodsCarouselService.save(paramModel);
        return R.ok(result);
        }

/**
 * 修改
 */
@PostMapping("/updateModel.json")
public R update(@RequestBody GoodsCarousel paramModel){
        boolean result= goodsCarouselService.updateById(paramModel);
        return R.ok(result);
        }

/**
 * 删除
 */
@PostMapping("/deleteModel.json")
public R delete(@RequestBody Integer[]ids){
        boolean result= goodsCarouselService.removeByIds(Arrays.asList(ids));
        return R.ok(result);
        }
        }
