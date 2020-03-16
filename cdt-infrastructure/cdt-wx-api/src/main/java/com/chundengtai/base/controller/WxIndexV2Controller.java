package com.chundengtai.base.controller;

import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.dao.ApiAttributeCategoryMapper;
import com.chundengtai.base.dto.AttributeCategoryDTO;
import com.chundengtai.base.dto.GoodsDTO;
import com.chundengtai.base.entity.AdVo;
import com.chundengtai.base.entity.AttributeCategoryVo;
import com.chundengtai.base.entity.GoodsVo;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.ApiAdService;
import com.chundengtai.base.service.ApiGoodsService;
import com.chundengtai.base.transfer.JsonTransfer;
import com.chundengtai.base.util.ApiBaseAction;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ApiAdService adService;

    @Autowired
    private ApiGoodsService goodsService;


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
            resultObj = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
            param.put("ad_position_id", 1);
            List<AdVo> banner = adService.queryList(param);
            resultObj.put("banner", banner);

            //分类
            param.clear();
            param.put("parent_id", 0);
            param.put("sidx", "sort_order");
            param.put("order", "desc");
            param.put("showPosition", 0);
            param.put("enabled", 1);
            PageHelper.startPage(0, 10, false);
            List<AttributeCategoryVo> categoryList = attributeCategoryMapper.queryList(param);
            resultObj.put("categoryList", mapperFacade.mapAsList(categoryList, AttributeCategoryDTO.class));

            //分类下面模块的商品
            param.clear();
            param.put("parent_id", 0);
            param.put("sidx", "sort_order");
            param.put("order", "desc");
            param.put("showPosition", 1);
            PageHelper.startPage(0, 5, false);
            List<AttributeCategoryVo> categoryGoodsList = attributeCategoryMapper.queryList(param);

            //查找其他分类下面的商品
            List<Map<String, Object>> newCategoryList = new ArrayList<>();
            for (AttributeCategoryVo categoryItem : categoryGoodsList) {
                param.clear();
                param.put("attribute_category", categoryItem.getId());
                param.put("sidx", "sort_order");
                param.put("order", "desc");
                param.put("fields", "id, name,list_pic_url,primary_pic_url,retail_price,market_price,browse,goods_brief");
                PageHelper.startPage(0, 6, false);
                List<GoodsVo> categoryGoods = goodsService.queryList(param);
                List<GoodsDTO> goodsDTOS = JsonTransfer.convertList(categoryGoods, GoodsDTO.class);
                Map<String, Object> newCategory = new HashMap<>();
                newCategory.put("id", categoryItem.getId());
                newCategory.put("name", categoryItem.getName());
                newCategory.put("showStyle", categoryItem.getShowStyle());
                newCategory.put("goodsList", goodsDTOS);
                newCategoryList.add(newCategory);
            }
            resultObj.put("productList", newCategoryList);
            log.info("首页数据查询完成");
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
            param.put("fields", "id, name,list_pic_url,primary_pic_url,retail_price,market_price,browse,goods_brief");
            PageHelper.startPage(0, 40, false);
            List<GoodsVo> newGoods = goodsService.queryList(param);
            goodsDTOS = mapperFacade.mapAsList(newGoods, GoodsDTO.class);
            redisTemplate.opsForValue().set("indexNewGoods", goodsDTOS, 10, TimeUnit.MINUTES);
            log.info("indexNewGoods数据库读取数据");
        }
        return Result.success(goodsDTOS);
    }

    @RequestMapping("/getBanner.json")
    @IgnoreAuth
    public Result<Map<String, Object>> getBannerContent(Integer bannerId) {

        AdVo adVo = adService.queryObject(bannerId);
        if (adVo == null) {
            return Result.failure();
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        if (adVo.getType() == 1) {
            String goodsId = adVo.getGoodsId();
            List<GoodsVo> goodsVos = new ArrayList<>();
            String[] good = goodsId.split(",");
            for (int i = 0; i < good.length; i++) {
                GoodsVo goodsVo = goodsService.queryObject(Integer.parseInt(good[i]));
                goodsVos.add(goodsVo);
            }
            hashMap.put("goods_show", goodsVos);
        }
        hashMap.put("content_show", adVo);
        return Result.success(hashMap);
    }

    private List<AdVo> getCollectByType(List<AdVo> banner, Integer type) {
        return banner.stream().filter(b ->
                b.getType().equals(type)
        ).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        String ss ="zhs";
        String[] a = ss.split(",");
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }
}
