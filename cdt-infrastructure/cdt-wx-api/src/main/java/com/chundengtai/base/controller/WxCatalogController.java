package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.bean.Goods;
import com.chundengtai.base.bean.dto.GoodsDto;
import com.chundengtai.base.entity.CategoryVo;
import com.chundengtai.base.entity.GoodsVo;
import com.chundengtai.base.service.ApiCategoryService;
import com.chundengtai.base.service.GoodsService;
import com.chundengtai.base.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Api(tags = "v2栏目")
@RestController
@RequestMapping("/apis/v2/catalog")
@Slf4j
public class WxCatalogController extends ApiBaseAction {

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private ApiCategoryService categoryService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 获取分类栏目数据
     */
    @ApiOperation(value = "获取分类栏目数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = false),
            @ApiImplicitParam(name = "page", value = "page", paramType = "query", required = false),
            @ApiImplicitParam(name = "size", value = "size", paramType = "query", required = false)})
    @GetMapping(value = "/index.json")
    @IgnoreAuth
    public Object index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> resultObj = (Map<String, Object>) redisTemplate.opsForValue().get("category");
        if (resultObj == null) {
            resultObj = new HashMap();
            Map params = new HashMap();
            params.put("sidx", "sort_order");
            params.put("order", "asc");
            params.put("parent_id", 0);
            //查询分类数据
            List<CategoryVo> data = categoryService.queryList(params);

            params.clear();
            params.put("sidx", "sort_order");
            params.put("order", "asc");


            data.stream().forEach(e -> {
                params.put("parent_id", e.getId());
                List<CategoryVo> categoryVos = categoryService.queryList(params);
                e.setSubCategoryList(categoryVos);
                //查询二级分类没有商品的过滤掉

            /*for (int i = 0; i < categoryVos.size(); i++) {
                List<Goods> goodsList = goodsService.list(new LambdaQueryWrapper<Goods>()
                        .eq(Goods::getCategoryId,categoryVos.get(i).getId()));
                if(goodsList.size()>0){
                    e.setSubCategoryList(categoryVos);
                }

            }*/

            });

            log.info("data======" + data);

            List<CategoryVo> categoryGoods = data.stream().filter(e -> e.getSubCategoryList() != null).collect(Collectors.toList());

            System.out.println("=====" + categoryGoods);

            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setId(0);
            categoryVo.setName("全部商品");
            categoryGoods.add(0, categoryVo);

            resultObj.put("goods", categoryGoods);
            redisTemplate.opsForValue().set("category", resultObj, 10, TimeUnit.MINUTES);
        }

        return toResponsSuccess(resultObj);
    }

    /**
     *
     */
    @ApiOperation(value = "分类目录当前分类数据接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true)})
    @GetMapping(value = "/current.json")
    @IgnoreAuth
    public Object current(Integer id) {
        Map<String, Object> resultObj = new HashMap();
        Map params = new HashMap();
        params.put("parent_id", id);
        CategoryVo currentCategory = null;
        if (null != id) {
            currentCategory = categoryService.queryObject(id);
        }

        //获取子分类数据
        if (null != currentCategory && null != currentCategory.getId()) {
            List<CategoryVo> categoryVoList = categoryService.queryList(params);


            currentCategory.setSubCategoryList(categoryVoList);
        }
        List<CategoryVo> categoryVoList = new ArrayList<>();
        categoryVoList.add(currentCategory);
        resultObj.put("currentCategory", categoryVoList);
        return toResponsSuccess(resultObj);
    }
}