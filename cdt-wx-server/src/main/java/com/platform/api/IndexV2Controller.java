package com.platform.api;

import com.chundengtai.base.result.Result;
import com.github.pagehelper.PageHelper;
import com.platform.annotation.IgnoreAuth;
import com.platform.dao.ApiAttributeCategoryMapper;
import com.platform.entity.AdVo;
import com.platform.entity.AttributeCategoryVo;
import com.platform.entity.GoodsVo;
import com.platform.service.ApiAdService;
import com.platform.service.ApiCategoryService;
import com.platform.service.ApiGoodsService;
import com.platform.util.ApiBaseAction;
import com.platform.util.BannerType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 窝边生活<br>
 */
@Api(value = "窝边生活版本", tags = "窝边生活首页")
@RestController
@RequestMapping("/api/v2/index")
@Slf4j
public class IndexV2Controller extends ApiBaseAction {
    @Autowired
    private ApiAdService adService;
    @Autowired
    private ApiGoodsService goodsService;
    @Autowired
    private ApiCategoryService categoryService;
    @Autowired
    private ApiAttributeCategoryMapper attributeCategoryMapper;

    /**
     * app首页
     */
    @ApiOperation(value = "首页包含轮播图、分类、分类下的商品、最新商品")
    @IgnoreAuth
    @GetMapping(value = "index")
    public Result<Map<String, Object>> index() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ad_position_id", 1);
        List<AdVo> banner = adService.queryList(param);
        List<AdVo> hotProduct = getCollectByType(banner, BannerType.HOT.getCode());
        List<AdVo> activity = getCollectByType(banner, BannerType.ACTIVITY.getCode());

        resultObj.put("hotProduct", hotProduct);
        resultObj.put("activity", activity);

        //分类
        param = new HashMap<String, Object>();
        param.put("parent_id", 0);
        param.put("sidx", "sort_order");
        param.put("order", "desc");
        param.put("showPosition", 0);
        PageHelper.startPage(0, 10, false);
//        List<CategoryVo> categoryList = categoryService.queryList(param);
        List<AttributeCategoryVo> categoryList = attributeCategoryMapper.queryList(param);
        resultObj.put("categoryList", categoryList);


        //分类下面模块的商品
        param = new HashMap<String, Object>();
        param.put("parent_id", 0);
        param.put("sidx", "sort_order");
        param.put("order", "desc");
        param.put("showPosition", 1);
        PageHelper.startPage(0, 5, false);
        List<AttributeCategoryVo> categoryGoodsList = attributeCategoryMapper.queryList(param);

        //查找其他分类下面的商品
        List<Map<String, Object>> newCategoryList = new ArrayList<>();
        for (AttributeCategoryVo categoryItem : categoryGoodsList) {
            List<GoodsVo> categoryGoods = new ArrayList<>();
            param = null;
            param = new HashMap<String, Object>();
            param.put("attribute_category", categoryItem.getId());
            param.put("sidx", "add_time");
            param.put("order", "desc");
            param.put("fields", "id as id, name as name, list_pic_url as list_pic_url, retail_price as retail_price,market_price as market_price");
            PageHelper.startPage(0, 4, false);
            categoryGoods = goodsService.queryList(param);

            Map<String, Object> newCategory = new HashMap<String, Object>();
            newCategory.put("id", categoryItem.getId());
            newCategory.put("name", categoryItem.getName());
            newCategory.put("showStyle", categoryItem.getShowStyle());
            newCategory.put("goodsList", categoryGoods);
            newCategoryList.add(newCategory);
        }
        resultObj.put("productList", newCategoryList);
        return Result.success(resultObj);
    }

    /**
     * app首页
     */
    @ApiOperation(value = "首页新品")
    @IgnoreAuth
    @GetMapping(value = "indexNewGoods")
    public Result<List<GoodsVo>> indexGoods() {
        //最新商品
        HashMap param = new HashMap<String, Object>();
        param.put("is_new", 1);
        param.put("is_delete", 0);
        param.put("is_on_sale", 1);
        param.put("sidx", "add_time");
        param.put("order", "desc");
        param.put("fields", "id, name, list_pic_url, retail_price");
        PageHelper.startPage(0, 10, false);
        List<GoodsVo> newGoods = goodsService.queryList(param);
        return Result.success(newGoods);
    }

    private List<AdVo> getCollectByType(List<AdVo> banner, Integer type) {
        return banner.stream().filter(b ->
                b.getType().equals(type)
        ).collect(Collectors.toList());
    }

    /**
     * app首页查看更多
     */
    @ApiOperation(value = "首页查看更多")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attribute_category", value = "商品类型id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pagesize", value = "每页显示数量 ", dataType = "int", paramType = "query")
    })
    @IgnoreAuth
    @GetMapping(value = "more")
    public Result<Map<String, Object>> indexMore(Integer attribute_category,
                                                 @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                 @RequestParam(value = "pagesize", defaultValue = "10") Integer pagesize) {
        //最新商品
        Map<String, Object> resultObj = new HashMap<String, Object>();
        HashMap param = new HashMap<String, Object>();
        param.put("attribute_category", attribute_category);
        param.put("sidx", "add_time");
        param.put("order", "desc");
        param.put("fields", "id as id, name as name, list_pic_url as list_pic_url, retail_price as retail_price,market_price as market_price");
        PageHelper.startPage(pageIndex, pagesize, false);
        List<GoodsVo> goodsVoList = goodsService.queryList(param);
        Map<String, Object> newCategory = new HashMap<String, Object>();
        newCategory.put("goodsList", goodsVoList);
        return Result.success(newCategory);
    }
}
