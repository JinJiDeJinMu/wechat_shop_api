package com.chundengtai.base.controller;

import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.annotation.LoginUser;
import com.chundengtai.base.dao.ApiAttributeCategoryMapper;
import com.chundengtai.base.dto.AttributeCategoryDTO;
import com.chundengtai.base.dto.GoodsDTO;
import com.chundengtai.base.entity.*;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.ApiAdService;
import com.chundengtai.base.service.ApiGoodsService;
import com.chundengtai.base.service.ApiProductService;
import com.chundengtai.base.service.ApiSearchHistoryService;
import com.chundengtai.base.transfer.JsonTransfer;
import com.chundengtai.base.util.ApiBaseAction;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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

    @Autowired
    private ApiSearchHistoryService searchHistoryService;

    @Autowired
    private ApiProductService apiProductService;


    /**
     * app首页
     */
    @ApiOperation(value = "首页包含轮播图、分类、分类下的商品、最新商品")
    @GetMapping(value = "index.json")
    @IgnoreAuth
    public Result<Map<String, Object>> index() {
        Map<String, Object> resultObj = (Map<String, Object>) redisTemplate.opsForValue().get("indexV2");
        if (resultObj == null) {
            resultObj = new HashMap<>();
            Map<String, Object> param = new HashMap<>();
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
            //resultObj.put("categoryList", mapperFacade.mapAsList(categoryList, AttributeCategoryDTO.class));
            resultObj.put("categoryList", null);
            //分类下面模块的商品
            param.clear();
            param.put("parent_id", 0);
            param.put("sidx", "sort_order");
            param.put("order", "desc");
            param.put("showPosition", 1);
            PageHelper.startPage(0, 8, false);
            List<AttributeCategoryVo> categoryGoodsList = attributeCategoryMapper.queryList(param);

            //查找其他分类下面的商品
            List<Map<String, Object>> newCategoryList = new ArrayList<>();
            for (AttributeCategoryVo categoryItem : categoryGoodsList) {
                param.clear();
                param.put("attribute_category", categoryItem.getId());
                param.put("sidx", "sort_order");
                param.put("order", "desc");
                param.put("limit",6);
                param.put("fields", "id, name,list_pic_url,primary_pic_url,retail_price,market_price,browse,goods_brief");
                PageHelper.startPage(0, 8, false);
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
            log.info("首页数据查询完成,indexV2="+resultObj);
            redisTemplate.opsForValue().set("indexV2", resultObj, 10, TimeUnit.MINUTES);
        }
        return Result.success(resultObj);
    }

    /**
     * app首页
     */
    @ApiOperation(value = "首页新品")
    @GetMapping(value = "indexNewGoods.json")
    @IgnoreAuth
    public Result<Map<String, Object>> indexGoods(String referrerId) {
        Map<String, Object> resultObj  = (Map<String, Object>) redisTemplate.opsForValue().get("indexNewGoods");
        if (resultObj == null) {
            resultObj = new HashMap<>();
            //最新商品
            HashMap param = new HashMap<String, Object>();
            param.put("is_new", 1);
            param.put("is_delete", 0);
            param.put("is_on_sale", 1);
            param.put("sidx", "add_time");
            param.put("order", "desc");
            param.put("limit",10);
            param.put("fields", "id, name,list_pic_url,primary_pic_url,retail_price,market_price,browse,goods_brief,goods_number");
            PageHelper.startPage(1, 10, false);
            List<GoodsVo> newGoods = goodsService.queryList(param);

            newGoods= newGoods.stream().filter(e->e.getGoods_number() >0).collect(Collectors.toList());
            List<GoodsDTO> goodsDTOS = mapperFacade.mapAsList(newGoods, GoodsDTO.class);

            resultObj.put("newGoods",goodsDTOS);
            param.clear();
            param.put("is_hot", 1);
            param.put("is_new", 0);
            param.put("is_delete", 0);
            param.put("is_on_sale", 1);
            param.put("sidx", "add_time");

            param.put("order", "desc");
            param.put("limit",2);
            param.put("fields", "id, name,list_pic_url,primary_pic_url,retail_price,market_price,browse,goods_brief,goods_number");
            PageHelper.startPage(0, 2, false);
            List<GoodsVo> hotGoods = goodsService.queryList(param);
            hotGoods= hotGoods.stream().filter(e->e.getGoods_number() >0).collect(Collectors.toList());
            List<GoodsDTO> hotgoodsDTOS = mapperFacade.mapAsList(hotGoods, GoodsDTO.class);
            resultObj.put("hotGoods",hotgoodsDTOS);
            redisTemplate.opsForValue().set("indexNewGoods",resultObj,5, TimeUnit.MINUTES);
        }
        return Result.success(resultObj);
    }

    /**
     * 首页推荐
     * @param
     * @return
     */
    @ApiOperation(value = "首页推荐")
    @GetMapping("/indexRelatedGoods.json")
    @IgnoreAuth
    public Result<List<GoodsDTO>> relatedGoods(/*@LoginUser UserVo userVo*/){

       /* Map<String, Object> params= new HashMap<>();
        params.put("user_id",userVo.getUserId());
        params.put("sidx","id");
        params.put("order","desc");
        params.put("limit",10);
        params.put("status",1);
        List<SearchHistoryVo> searchHistoryVoList = searchHistoryService.queryList(params);
        List<Integer> goodIds= new ArrayList<>();
        List<Integer> goodList= new ArrayList<>();

        List<GoodsVo> goodsVos = new ArrayList<>();
        List<GoodsDTO> tuijian = new ArrayList<>();
        if(searchHistoryVoList == null){
            HashMap param = new HashMap<String, Object>();
            param.put("is_new", 1);
            param.put("is_delete", 0);
            param.put("is_on_sale", 1);
            param.put("sidx", "add_time");
            param.put("order", "desc");
            param.put("fields", "id, name,list_pic_url,primary_pic_url,retail_price,market_price,browse,goods_brief");
            PageHelper.startPage(2, 10, false);
            List<GoodsVo> newGoods = goodsService.queryList(param);
            tuijian = mapperFacade.mapAsList(newGoods, GoodsDTO.class);

        }else{
            searchHistoryVoList.forEach(e->{
                change(goodIds,e.getGoods());
            });

            //随机6个
            if(goodIds.size() >=6){
                goodList = randomSub(goodIds,6);
            }
            goodList.forEach(e->{
                    GoodsVo goodsVo = goodsService.queryObject(e);
                    goodsVos.add(goodsVo);
            });

            tuijian = mapperFacade.mapAsList(goodsVos, GoodsDTO.class);
        }

        return Result.success(tuijian);*/
       return Result.success(null);
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

    private void change(List<Integer> list,String goodIds){
        String[] str = goodIds.split(",");
        for (int i = 0; i < str.length; i++) {
            if(!list.contains(Integer.parseInt(str[i]))){
                list.add(Integer.parseInt(str[i]));
            }
        }
    }
    public static <T> List randomSub(List<T> tl, int n) {
        int count = tl.size()>n?n:tl.size();
        Random r = ThreadLocalRandom.current();
        //避免修改原列表
        List<T> temp = new ArrayList<>(tl);
        //记录返回列表
        List<T> list = new ArrayList<>(count);
        for (int i=0;i<count;i++) {
            int t=r.nextInt(temp.size());
            list.add(temp.get(t));
            temp.remove(t);
        }
        return list;
    }
    public static void main(String[] args) {

        Random random = new Random();
        random.nextInt(4);
        System.out.println(random.nextInt(4));
    }
}
