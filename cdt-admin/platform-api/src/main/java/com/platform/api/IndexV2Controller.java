package com.platform.api;

import com.chundengtai.base.result.Result;
import com.github.pagehelper.PageHelper;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.AdVo;
import com.platform.entity.CategoryVo;
import com.platform.entity.GoodsVo;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import com.platform.util.BannerType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 窝边生活<br>
 */
@Api(tags = "窝边生活首页")
@RestController
@RequestMapping("/api/v2/index")
@Slf4j
public class IndexV2Controller extends ApiBaseAction {
    @Autowired
    private ApiAdService adService;
    @Autowired
    private ApiChannelService channelService;
    @Autowired
    private ApiGoodsService goodsService;
    @Autowired
    private ApiBrandService brandService;
    @Autowired
    private ApiTopicService topicService;
    @Autowired
    private ApiCategoryService categoryService;
    @Autowired
    private ApiCartService cartService;
    @Autowired
    private LifeServiceSer lifeServiceSer;

    /**
     * app首页
     */
    @ApiOperation(value = "首页")
    @IgnoreAuth
    @GetMapping(value = "index")
    public Object index() {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ad_position_id", 1);
        List<AdVo> banner = adService.queryList(param);
        List<AdVo> hotProduct = getCollectByType(banner, BannerType.HOT.getCode());
        List<AdVo> activity = getCollectByType(banner, BannerType.ACTIVITY.getCode());

        resultObj.put("hotProduct", hotProduct);
        resultObj.put("activity", activity);

        //分类及其下面的商品
        param = new HashMap<String, Object>();
        param.put("parent_id", 0);
        param.put("sidx", "sort_order");
        param.put("order", "desc");
        PageHelper.startPage(0, 5, false);
        List<CategoryVo> categoryList = categoryService.queryList(param);
        List<Map<String, Object>> newCategoryList = new ArrayList<>();

        for (CategoryVo categoryItem : categoryList) {
            List<GoodsVo> categoryGoods = new ArrayList<>();
            param = null;
            param = new HashMap<String, Object>();
            param.put("category_id", categoryItem.getId());
            param.put("fields", "id as id, name as name, list_pic_url as list_pic_url, retail_price as retail_price");
            PageHelper.startPage(0, 7, false);
            categoryGoods = goodsService.queryList(param);

            Map<String, Object> newCategory = new HashMap<String, Object>();
            newCategory.put("id", categoryItem.getId());
            newCategory.put("name", categoryItem.getName());
            newCategory.put("goodsList", categoryGoods);
            newCategoryList.add(newCategory);
        }
        resultObj.put("categoryList", newCategoryList);

        //最新商品
        param = new HashMap<String, Object>();
        param.put("is_new", 1);
        param.put("is_delete", 0);
        param.put("is_on_sale", 1);
        param.put("sidx", "sort_order");
        param.put("order", "desc");
        param.put("fields", "id, name, list_pic_url, retail_price");
        PageHelper.startPage(0, 4, false);
        List<GoodsVo> newGoods = goodsService.queryList(param);
        resultObj.put("newGoodsList", newGoods);

        return Result.success(resultObj);
    }

    private List<AdVo> getCollectByType(List<AdVo> banner, Integer type) {
        return banner.stream().filter(b ->
                b.getType().equals(type)
        ).collect(Collectors.toList());
    }
}
