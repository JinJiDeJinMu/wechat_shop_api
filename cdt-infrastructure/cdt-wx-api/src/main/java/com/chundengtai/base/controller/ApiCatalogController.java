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
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "栏目")
@RestController
@RequestMapping("/api/v2/catalog")
public class ApiCatalogController extends ApiBaseAction {
    @Autowired
    private ApiCategoryService categoryService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 获取分类栏目数据
     */
    @ApiOperation(value = "获取分类栏目数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = false),
            @ApiImplicitParam(name = "page", value = "page", paramType = "query", required = false),
            @ApiImplicitParam(name = "size", value = "size", paramType = "query", required = false)})
    @IgnoreAuth
    @GetMapping(value = "index")
    public Object index(Integer id,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> resultObj = new HashMap();
        Map params = new HashMap();
        params.put("sidx", "sort_order");
        params.put("order", "asc");
        params.put("parent_id",0);
        //查询分类数据
        List<CategoryVo> data = categoryService.queryList(params);

        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setId(0);
        categoryVo.setName("全部商品");
        data.add(0,categoryVo);

        params.clear();
        params.put("sidx", "sort_order");
        params.put("order", "asc");

        List<CategoryVo> categoryGoods = data;
        categoryGoods.stream().forEach(e ->{
            params.put("parent_id",e.getId());
            List<CategoryVo> categoryVos = categoryService.queryList(params);
            e.setSubCategoryList(categoryVos);
        });

        System.out.println("------------"+categoryGoods);
        Iterator<CategoryVo> tt = categoryGoods.iterator();
        while (tt.hasNext()){
            if(!tt.next().getName().equals("全部商品")){
                if(tt.next().getSubCategoryList() == null || tt.next().getSubCategoryList().size()<=0){
                    tt.remove();
                }
            }
        }
        System.out.println("====="+categoryGoods);
        resultObj.put("goods", categoryGoods);

        return toResponsSuccess(resultObj);
    }

    /**
     *
     */
    @ApiOperation(value = "分类目录当前分类数据接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = false)})
    @IgnoreAuth
    @GetMapping(value = "current")
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