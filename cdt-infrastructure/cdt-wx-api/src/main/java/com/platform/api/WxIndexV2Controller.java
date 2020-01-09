package com.platform.api;

import com.chundengtai.base.dto.AttributeCategoryDTO;
import com.chundengtai.base.dto.GoodsDTO;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.transfer.JsonTransfer;
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
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 窝边生活<br>
 */
@Api(value = "窝边生活版本", tags = "窝边生活首页")
@RestController
@RequestMapping("/apis/v2/index")
@Slf4j
public class WxIndexV2Controller extends ApiBaseAction {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ApiAdService adService;

    @Autowired
    private ApiGoodsService goodsService;

    @Autowired
    private ApiCategoryService categoryService;

    @Autowired
    private ApiAttributeCategoryMapper attributeCategoryMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * app首页
     */
    @ApiOperation(value = "首页包含轮播图、分类、分类下的商品、最新商品")
    @IgnoreAuth
    @GetMapping(value = "index.json")
    public Result<Map<String, Object>> index() {
        Map<String, Object> resultObj = (Map<String, Object>) redisTemplate.opsForValue().get("indexV2");
        if (resultObj == null) {
            resultObj = new HashMap<String, Object>();
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
            param.put("enabled", 1);
            PageHelper.startPage(0, 10, false);
            List<AttributeCategoryVo> categoryList = attributeCategoryMapper.queryList(param);
            resultObj.put("categoryList", mapperFacade.mapAsList(categoryList, AttributeCategoryDTO.class));

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
                param.put("sidx", "sort_order");
                param.put("order", "desc");
                param.put("fields", "id as id, name as name, list_pic_url as list_pic_url,primary_pic_url,retail_price as retail_price,market_price as market_price");
                PageHelper.startPage(0, 6, false);
                categoryGoods = goodsService.queryList(param);

                List<GoodsDTO> goodsDTOS = JsonTransfer.convertList(categoryGoods, GoodsDTO.class);
                Map<String, Object> newCategory = new HashMap<String, Object>();
                newCategory.put("id", categoryItem.getId());
                newCategory.put("name", categoryItem.getName());
                newCategory.put("showStyle", categoryItem.getShowStyle());
                newCategory.put("goodsList", goodsDTOS);
                newCategoryList.add(newCategory);
            }
            resultObj.put("productList", newCategoryList);
            log.info("indexv2数据库读取数据");
            redisTemplate.opsForValue().set("indexV2", resultObj, 10, TimeUnit.MINUTES);
        }
        return Result.success(resultObj);
    }

    /**
     * app首页
     */
    @ApiOperation(value = "首页新品")
    @IgnoreAuth
    @GetMapping(value = "indexNewGoods.json")
    public Result<List<GoodsDTO>> indexGoods(String referrerId) {
        List<GoodsDTO> goodsDTOS = (List<GoodsDTO>) redisTemplate.opsForValue().get("indexNewGoods");
        if (goodsDTOS == null) {
            //最新商品
            HashMap param = new HashMap<String, Object>();
            param.put("is_new", 1);
            param.put("is_delete", 0);
            param.put("is_on_sale", 1);
            param.put("sidx", "add_time");
            param.put("order", "desc");
            param.put("fields", "id, name,list_pic_url,primary_pic_url,retail_price,market_price");
            PageHelper.startPage(0, 300, false);
            List<GoodsVo> newGoods = goodsService.queryList(param);
            goodsDTOS = mapperFacade.mapAsList(newGoods, GoodsDTO.class);
            redisTemplate.opsForValue().set("indexNewGoods", goodsDTOS, 10, TimeUnit.MINUTES);
            log.info("indexNewGoods数据库读取数据");
        }
        return Result.success(goodsDTOS);
    }

    private List<AdVo> getCollectByType(List<AdVo> banner, Integer type) {
        return banner.stream().filter(b ->
                b.getType().equals(type)
        ).collect(Collectors.toList());
    }

}
