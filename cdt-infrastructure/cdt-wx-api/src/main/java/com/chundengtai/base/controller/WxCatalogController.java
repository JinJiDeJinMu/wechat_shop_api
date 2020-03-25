package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.bean.Goods;
import com.chundengtai.base.bean.dto.GoodsDto;
import com.chundengtai.base.entity.CategoryVo;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Object index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
       // Map<String, Object> resultObj = (Map<String, Object>) redisTemplate.opsForValue().get("categoryData");
       // if(resultObj == null) {
        Map<String, Object> resultObj = new HashMap();
            Map params = new HashMap();
            params.put("page", page);
            params.put("limit", size);
            params.put("sidx", "sort_order");
            params.put("order", "asc");
            params.put("parent_id",0);
            //查询分类数据
            List<CategoryVo> data = categoryService.queryList(params);

           /* List<CategoryVo> categoryGoods = categoryVoList.stream().filter(e -> e.getParent_id() > 0)
                    .collect(Collectors.toList());

            categoryGoods.forEach(e -> {
                List<Goods> goods = goodsService.list(new QueryWrapper<Goods>().lambda()
                        .eq(Goods::getCategoryId, e.getId())
                );

                List<GoodsDto> goodsDTOList = mapperFacade.mapAsList(goods, GoodsDto.class);
                e.setGoodsDtoList(goodsDTOList);
            });*/
            CategoryVo currentCategory = null;
            if (null == currentCategory && null != data && data.size() != 0) {
                currentCategory = data.get(0);
            } else {
                currentCategory = new CategoryVo();
            }
           System.out.println("========"+data.get(0));

            //获取子分类数据
            if (null != currentCategory && null != currentCategory.getId()) {
                params.put("parent_id", currentCategory.getId());
                currentCategory.setSubCategoryList(categoryService.queryList(params));
            }
            resultObj.put("categoryList", data);
            resultObj.put("currentCategory", currentCategory);
            log.info("分类数据查询完成,categoryData="+resultObj);
            /*redisTemplate.opsForValue().set("categoryData", resultObj, 5, TimeUnit.MINUTES);*/
        //}
        return toResponsSuccess(resultObj);
    }

    /**
     *
     */
    @ApiOperation(value = "分类目录当前分类数据接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = true)})
    @GetMapping(value = "/current.json")
    public Object current(Integer id) {
       /* Map<String, Object> resultObj = new HashMap();
        Map params = new HashMap();
        params.put("parent_id", id);
        List<CategoryVo> categoryVoList = categoryService.queryList(params);

        categoryVoList.stream().forEach(e->{
            List<Goods> goods = goodsService.list(new QueryWrapper<Goods>().lambda()
                    .eq(Goods::getCategoryId, e.getId())
            );

            List<GoodsDto> goodsDTOList = mapperFacade.mapAsList(goods, GoodsDto.class);
            e.setGoodsDtoList(goodsDTOList);
        });

        resultObj.put("categoryGoods", categoryVoList);
        return toResponsSuccess(resultObj);*/
        Map<String, Object> resultObj = new HashMap();
        Map params = new HashMap();
        params.put("parent_id", 0);
        CategoryVo currentCategory = null;
        if (null != id) {
            currentCategory = categoryService.queryObject(id);
        }
        //获取子分类数据
        if (null != currentCategory && null != currentCategory.getId()) {
            params.put("parent_id", currentCategory.getId());
            currentCategory.setSubCategoryList(categoryService.queryList(params));
        }
        resultObj.put("currentCategory", currentCategory);
        return toResponsSuccess(resultObj);
    }
}