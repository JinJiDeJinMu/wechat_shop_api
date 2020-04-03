package com.chundengtai.base.controller;

import com.chundengtai.base.annotation.APPLoginUser;
import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.annotation.LoginUser;
import com.chundengtai.base.dto.GoodsDTO;
import com.chundengtai.base.entity.*;
import com.chundengtai.base.oss.OSSFactory;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.*;
import com.chundengtai.base.util.*;
import com.chundengtai.base.utils.Query;
import com.chundengtai.base.utils.RRException;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.text.AttributedString;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Api(tags = "商品管理")
@RestController
@RequestMapping("/api/goods")
public class ApiGoodsController extends ApiBaseAction {
    @Autowired
    private ApiGoodsService goodsService;
    @Autowired
    private ApiGoodsSpecificationService goodsSpecificationService;
    @Autowired
    private ApiProductService productService;
    @Autowired
    private ApiGoodsGalleryService goodsGalleryService;
    @Autowired
    private ApiAttributeService attributeService;
    @Autowired
    private ApiBrandService brandService;
    @Autowired
    private ApiCollectService collectService;
    @Autowired
    private ApiFootprintService footprintService;
    @Autowired
    private ApiCategoryService categoryService;
    @Autowired
    private ApiSearchHistoryService searchHistoryService;
    @Autowired
    private ApiRelatedGoodsService relatedGoodsService;
    @Autowired
    private ApiCartService cartService;
    @Autowired
    private ApiProductService apiProductService;

    private List<File> file;

    private List<String> fileFileName;

    private List<String> fileContentType;

    /**
     *
     */
    @ApiOperation(value = "商品首页")
    @IgnoreAuth
    @GetMapping(value = "index")
    public Object index(Integer brand_id) {
        Map param = new HashMap();
        param.put("is_delete", 0);
        param.put("is_on_sale", 1);
        List<GoodsVo> goodsList = goodsService.queryList(param);
        return toResponsSuccess(goodsList);
    }

    /**
     * 秒杀产品列表
     */
    @ApiOperation(value = "秒杀产品")
    @IgnoreAuth
    @GetMapping(value = "kill")
    public Object kill(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size,
                       String sort, String order) {

        //查询列表数据
        Map params = new HashMap();
        params.put("page", page);
        params.put("limit", size);
        params.put("is_secKill", GoodsTypeEnum.SECONDS_KILL.getCode());// 秒杀
        params.put("sidx", "start_time");
        params.put("order", "asc");

        Query query = new Query(params);
        List<GoodsVo> killlist = goodsService.queryKillPage(query);
        int total = goodsService.queryKillTotal(query);

        ApiPageUtils goodsData = new ApiPageUtils(killlist, total, query.getLimit(), query.getPage());
        goodsData.setGoodsList(goodsData.getData());
        return toResponsSuccess(goodsData);
    }

    /**
     * 团购产品列表
     */
    @ApiOperation(value = "团购产品")
    @IgnoreAuth
    @GetMapping(value = "group")
    public Object group(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size,
                        String sort, String order) {
        //查询列表数据
        Map params = new HashMap();
        params.put("page", page);
        params.put("limit", size);
        params.put("is_secKill", GoodsTypeEnum.GROUP_GOODS.getCode());// 团购
        params.put("sidx", "id");
        params.put("order", "asc");

        Query query = new Query(params);
        List<GoodsVo> grouplist = goodsService.queryGroupList(query);
        int total = goodsService.queryGroupTotal(query);

        ApiPageUtils goodsData = new ApiPageUtils(grouplist, total, query.getLimit(), query.getPage());
        goodsData.setGoodsList(goodsData.getData());
        return toResponsSuccess(goodsData);
    }


    /**
     * 获取商品规格信息，用于购物车编辑时选择规格
     */
    @ApiOperation(value = " 获取商品规格信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "商品id", paramType = "path", required = true)})
    @IgnoreAuth
    @GetMapping(value = "sku")
    public Object sku(Integer id) {
        Map<String, Object> resultObj = new HashMap();
        Map param = new HashMap();
        param.put("goods_id", id);
        List<GoodsSpecificationVo> goodsSpecificationEntityList = goodsSpecificationService.queryList(param);
        List<ProductVo> productEntityList = productService.queryList(param);
        resultObj.put("specificationList", goodsSpecificationEntityList);
        resultObj.put("productList", productEntityList);
        return toResponsSuccess(resultObj);
    }

    /**
     * 商品详情页数据
     */
    @ApiOperation(value = " 商品详情页数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "商品id", paramType = "path", required = true),
            @ApiImplicitParam(name = "referrer", value = "商品referrer", paramType = "path", required = false)})
    @GetMapping(value = "detail")
    public Object detail(Integer id, Long referrer) {

        Map<String, Object> resultObj = new HashMap();
        Long userId = getUserId();
        GoodsVo info = goodsService.queryObject(id);
        if (info == null) return Result.failure("数据不存在!");
        goodsService.updateBrowse(info);
        /*Long mid = info.getMerchantId();
        //添加商家信息
        CdtMerchantEntity cdtMerchant = cdtMerchantService.queryObject(info.getMerchantId());
        resultObj.put("merchantInfo", cdtMerchant);*/

        Map param = new HashMap();
        param.put("goods_id", id);

        Map specificationParam = new HashMap();
        specificationParam.put("fields", "gs.*, s.name");
        specificationParam.put("goods_id", id);
        specificationParam.put("specification", true);
        specificationParam.put("order", "asc");
        List<GoodsSpecificationVo> goodsSpecificationEntityList = goodsSpecificationService.queryList(specificationParam);

        List<Map> specificationList = new ArrayList();
        //按规格名称分组
        for (int i = 0; i < goodsSpecificationEntityList.size(); i++) {
            GoodsSpecificationVo specItem = goodsSpecificationEntityList.get(i);
            List<GoodsSpecificationVo> tempList = null;
            for (int j = 0; j < specificationList.size(); j++) {
                if (specificationList.get(j).get("specification_id").equals(specItem.getSpecification_id())) {
                    tempList = (List<GoodsSpecificationVo>) specificationList.get(j).get("valueList");
                    break;
                }
            }
            if (null == tempList) {
                Map temp = new HashMap();
                temp.put("specification_id", specItem.getSpecification_id());
                temp.put("name", specItem.getName());
                temp.put("pic_url", specItem.getPic_url());
                tempList = new ArrayList();
                tempList.add(specItem);
                temp.put("valueList", tempList);
                specificationList.add(temp);
            } else {
                for (int j = 0; j < specificationList.size(); j++) {
                    if (specificationList.get(j).get("specification_id").equals(specItem.getSpecification_id())) {
                        tempList = (List<GoodsSpecificationVo>) specificationList.get(j).get("valueList");
                        tempList.add(specItem);
                        break;
                    }
                }
            }
        }

        //商品详细信息
        List<ProductVo> productEntityList = productService.queryList(param);
        if(productEntityList.size() == 1){
            info.setRetail_price(productEntityList.get(0).getRetail_price());
        }else {
            Optional<BigDecimal> result = productEntityList.stream().map(ProductVo::getRetail_price).min(BigDecimal::compareTo);
            List<ProductVo> collect = productEntityList.stream().filter(e -> e.getRetail_price().compareTo(result.get()) == 0).collect(Collectors.toList());
            info.setRetail_price(collect.get(0).getRetail_price());
        }
        Integer number = productEntityList.stream().mapToInt(ProductVo::getGoods_number).sum();
        info.setGoods_number(number);
        resultObj.put("info", info);

        List<GoodsGalleryVo> gallery = goodsGalleryService.queryList(param);
        Map ngaParam = new HashMap();
        ngaParam.put("fields", "nga.value, na.name");
        ngaParam.put("sidx", "nga.id");
        ngaParam.put("order", "asc");
        ngaParam.put("goods_id", id);
        List<AttributeVo> attribute = attributeService.queryList(ngaParam);

        //todo:商品问答注释掉
        //Map issueParam = new HashMap();
        //issueParam.put("goods_id", id);
        //List<GoodsIssueVo> issue = goodsIssueService.queryList(issueParam);
        //resultObj.put("issue", issue);
        //BrandVo brand = brandService.queryObject(info.getBrand_id());

        param.put("value_id", id);
        param.put("type_id", 0);

        //当前用户是否收藏
        Map collectParam = new HashMap();
        collectParam.put("user_id", getUserId());
        collectParam.put("value_id", id);
        collectParam.put("type_id", 0);
        Integer userHasCollect = collectService.queryTotal(collectParam);
        if (userHasCollect > 0) {
            userHasCollect = 1;
        }

        //记录用户的足迹
        FootprintVo footprintEntity = new FootprintVo();
        footprintEntity.setAdd_time(System.currentTimeMillis() / 1000);
        footprintEntity.setGoods_brief(info.getGoods_brief());
        footprintEntity.setList_pic_url(info.getList_pic_url());
        footprintEntity.setGoods_id(info.getId());
        footprintEntity.setName(info.getName());
        footprintEntity.setRetail_price(info.getRetail_price());
        footprintEntity.setUser_id(userId);
        if (null != referrer) {
            footprintEntity.setReferrer(referrer);
        } else {
            footprintEntity.setReferrer(0L);
        }

        footprintService.save(footprintEntity);
        resultObj.put("gallery", gallery);
        resultObj.put("attribute", attribute);
        resultObj.put("userHasCollect", userHasCollect);

        //resultObj.put("comment", comment);
        //resultObj.put("brand", brand);

        resultObj.put("specificationList", specificationList);
        resultObj.put("productList", productEntityList);

        // 记录推荐人是否可以领取红包，用户登录时校验
//        try {
//            // 是否已经有可用的转发红包
//            Map params = new HashMap();
//            params.put("user_id", userId);
//            params.put("send_type", 2);
//            params.put("unUsed", true);
//            List<CouponVo> enabledCouponVos = apiCouponService.queryUserCoupons(params);
//            if ((null == enabledCouponVos || enabledCouponVos.size() == 0)
//                    && null != referrer && null != userId) {
//                // 获取优惠信息提示
//                Map couponParam = new HashMap();
//                couponParam.put("enabled", true);
//                Integer[] send_types = new Integer[]{2};
//                couponParam.put("send_types", send_types);
//                List<CouponVo> couponVos = apiCouponService.queryList(couponParam);
//                if (null != couponVos && couponVos.size() > 0) {
//                    CouponVo couponVo = couponVos.get(0);
//                    Map footprintParam = new HashMap();
//                    footprintParam.put("goods_id", id);
//                    footprintParam.put("referrer", referrer);
//                    Integer footprintNum = footprintService.queryTotal(footprintParam);
//                    if (null != footprintNum && null != couponVo.getMin_transmit_num()
//                            && footprintNum > couponVo.getMin_transmit_num()) {
//                        UserCouponVo userCouponVo = new UserCouponVo();
//                        userCouponVo.setAdd_time(new Date());
//                        userCouponVo.setCoupon_id(couponVo.getId());
//                        userCouponVo.setCoupon_number(CharUtil.getRandomString(12));
//                        userCouponVo.setUser_id(getUserId());
//                        apiUserCouponService.save(userCouponVo);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return toResponsSuccess(resultObj);
    }

    /**
     * 　获取分类下的商品
     */
    @ApiOperation(value = " 获取分类下的商品")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "分类id", paramType = "path", required = true)})
    @IgnoreAuth
    @GetMapping(value = "category")
    public Object category(Integer id) {
        Map<String, Object> resultObj = new HashMap();
        //
        CategoryVo currentCategory = categoryService.queryObject(id);
        //
        CategoryVo parentCategory = categoryService.queryObject(currentCategory.getParent_id());
        Map params = new HashMap();
        params.put("parent_id", currentCategory.getParent_id());
        List<CategoryVo> brotherCategory = categoryService.queryList(params);
        //
        resultObj.put("currentCategory", currentCategory);
        resultObj.put("parentCategory", parentCategory);
        resultObj.put("brotherCategory", brotherCategory);
        return toResponsSuccess(resultObj);
    }

    /**
     * 　　获取商品列表
     */
    @ApiOperation(value = "获取商品列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "categoryId", value = "分类id", paramType = "path", required = true),
            @ApiImplicitParam(name = "brandId", value = "品牌Id", paramType = "path", required = true),
            @ApiImplicitParam(name = "isNew", value = "新商品", paramType = "path", required = true),
            @ApiImplicitParam(name = "isHot", value = "热卖商品", paramType = "path", required = true)})
    @IgnoreAuth
    @GetMapping(value = "list")
    public Object list(@LoginUser UserVo loginUser,
                       Integer categoryId,
                       Integer attriId,
                       Integer brandId,
                       String keyword,
                       Integer isNew,
                       Integer isHot,
                       @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size,
                       String sort, String order) {
        Map params = new HashMap();
        params.put("is_delete", 0);
        params.put("is_on_sale", 1);
        params.put("order", sort);
        params.put("sidx", order);
        //
        if (null != sort && sort.equals("price")) {
            params.put("sidx", "retail_price");
            params.put("order", order);
        } else {
            params.put("sidx", "id");
            params.put("order", "desc");
        }

        if(null !=categoryId && categoryId>0){
            params.put("category_id", categoryId);
        }

        if(null != attriId && attriId>0){
            params.put("attribute_category", attriId);
        }
        System.out.println("======="+params);
        //添加到搜索历史
        if (!StringUtils.isNullOrEmpty(keyword)) {
            SearchHistoryVo searchHistoryVo = new SearchHistoryVo();
            searchHistoryVo.setAdd_time(System.currentTimeMillis() / 1000);
            searchHistoryVo.setKeyword(keyword);
            searchHistoryVo.setUser_id(null != loginUser ? loginUser.getUserId().toString() : "");
            searchHistoryVo.setFrom("");
            searchHistoryService.save(searchHistoryVo);

        }
        //筛选的分类
        List<CategoryVo> filterCategory = new ArrayList();
        CategoryVo rootCategory = new CategoryVo();
        rootCategory.setId(0);
        rootCategory.setName("全部");
        rootCategory.setChecked(false);
        filterCategory.add(rootCategory);

        //查询列表数据
        params.put("fields", "id, name, list_pic_url,primary_pic_url,market_price,retail_price, goods_brief,is_service,add_time,browse");
        //Query query = new Query(params);
        PageHelper.startPage(page, size);
        List<GoodsVo> goodsList = goodsService.queryList(params);
        ApiPageUtils goodsData = new ApiPageUtils(new PageInfo(goodsList));
        goodsData.setFilterCategory(filterCategory);
        goodsData.setGoodsList(goodsList);
        return toResponsSuccess(goodsData);
    }

    //查询新品列表
    @ApiOperation(value = "首页新品")
    @GetMapping(value = "NewGoods")
    public Result<Map<String, Object>> indexGoods(String referrerId) {

            Map<String, Object> resultObj = new HashMap<>();
            //最新商品
            HashMap param = new HashMap<String, Object>();
            param.put("is_new", 1);
            param.put("is_delete", 0);
            param.put("is_on_sale", 1);
            param.put("sidx", "add_time");
            param.put("order", "desc");
            param.put("fields", "id, name,list_pic_url,primary_pic_url,retail_price,market_price,browse,goods_brief");

            param.put("offset",0);
            param.put("limit",40);
            List<GoodsVo> newGoods = goodsService.queryList(param);

            resultObj.put("goodsList",newGoods);
            return Result.success(resultObj);
    }

    /**
     * 　　商品列表筛选的分类列表
     */
    @ApiOperation(value = "商品列表筛选的分类列表")
    @IgnoreAuth
    @GetMapping(value = "filter")
    public Object filter(Integer categoryId,
                         String keyword, Integer isNew, Integer isHot) {
        Map params = new HashMap();
        params.put("is_delete", 0);
        params.put("is_on_sale", 1);
        params.put("categoryId", categoryId);
        params.put("keyword", keyword);
        params.put("isNew", isNew);
        params.put("isHot", isHot);
        if (null != categoryId) {
            Map categoryParams = new HashMap();
            categoryParams.put("categoryId", categoryId);
            List<CategoryVo> categoryEntityList = categoryService.queryList(categoryParams);
            List<Integer> category_ids = new ArrayList();
            for (CategoryVo categoryEntity : categoryEntityList) {
                category_ids.add(categoryEntity.getId());
            }
            params.put("category_id", category_ids);
        }
        //筛选的分类
        List<CategoryVo> filterCategory = new ArrayList();
        CategoryVo rootCategory = new CategoryVo();
        rootCategory.setId(0);
        rootCategory.setName("全部");
        // 二级分类id
        List<GoodsVo> goodsEntityList = goodsService.queryList(params);
        if (null != goodsEntityList && goodsEntityList.size() > 0) {
            List<Integer> categoryIds = new ArrayList();
            for (GoodsVo goodsEntity : goodsEntityList) {
                categoryIds.add(goodsEntity.getCategory_id());
            }
            //查找二级分类的parent_id
            Map categoryParam = new HashMap();
            categoryParam.put("categoryIds", categoryIds);
            List<CategoryVo> parentCategoryList = categoryService.queryList(categoryParam);
            //
            List<Integer> parentIds = new ArrayList();
            for (CategoryVo categoryEntity : parentCategoryList) {
                parentIds.add(categoryEntity.getId());
            }
            //一级分类
            categoryParam.put("categoryIds", parentIds);
            List<CategoryVo> parentCategory = categoryService.queryList(categoryParam);
            if (null != parentCategory) {
                filterCategory.addAll(parentCategory);
            }
        }
        return toResponsSuccess(filterCategory);
    }

    /**
     * 　　新品首发
     */
    @ApiOperation(value = "新品首发")
    @IgnoreAuth
    @GetMapping(value = "new")
    public Object newAction() {
        Map<String, Object> resultObj = new HashMap();
        Map bannerInfo = new HashMap();
        bannerInfo.put("url", "");
        bannerInfo.put("name", "坚持初心，为你寻觅世间好物");
        bannerInfo.put("img_url", "https://platform-wxmall.oss-cn-beijing.aliyuncs.com/upload/20180727/1504208321fef4.png");
        resultObj.put("bannerInfo", bannerInfo);
        return toResponsSuccess(resultObj);
    }

    /**
     * 　　为您推荐
     */
    @ApiOperation(value = "为您推荐")
    @ApiImplicitParams({@ApiImplicitParam(name = "attribute_category", value = "分类id", paramType = "path", required = true)})
    @IgnoreAuth
    @GetMapping(value = "hot")
    public Object hot(Integer attribute_category) {
        Map<String, Object> resultObj = new HashMap();
        List<Map<String, Object>> newCategoryList = new ArrayList<>();
        List<GoodsVo> categoryGoods = new ArrayList<>();
        Map param = new HashMap<String, Object>();
        param.put("attribute_category", attribute_category);
        param.put("sidx", "add_time");
        param.put("order", "desc");
        param.put("fields", "id as id, name as name, list_pic_url as list_pic_url,primary_pic_url,retail_price as retail_price");
        PageHelper.startPage(0, 6, false);
        categoryGoods = goodsService.queryList(param);

        Map<String, Object> newCategory = new HashMap<String, Object>();
        newCategory.put("goodsList", categoryGoods);
        resultObj.put("newCategory", newCategory);
        return toResponsSuccess(resultObj);
    }

    /**
     * 　　商品详情页的大家都在看的商品
     */
    @ApiOperation(value = "商品详情页")
    @GetMapping(value = "related")
    public Object related(Integer id) {
        Map<String, Object> resultObj = new HashMap();
        Map param = new HashMap();
        param.put("goods_id", id);
        param.put("fields", "related_goods_id");
        List<RelatedGoodsVo> relatedGoodsEntityList = relatedGoodsService.queryList(param);

        List<Integer> relatedGoodsIds = new ArrayList();
        for (RelatedGoodsVo relatedGoodsEntity : relatedGoodsEntityList) {
            relatedGoodsIds.add(relatedGoodsEntity.getRelated_goods_id());
        }

        List<GoodsVo> relatedGoods = new ArrayList<GoodsVo>();

        if (null == relatedGoodsIds || relatedGoods.size() < 1) {
            //查找同分类下的商品
            GoodsVo goodsCategory = goodsService.queryObject(id);
            Map paramRelated = new HashMap();
            paramRelated.put("fields", "id, name, list_pic_url,primary_pic_url,retail_price");
            paramRelated.put("category_id", goodsCategory.getCategory_id());
            relatedGoods = goodsService.queryList(paramRelated);
        } else {
            Map paramRelated = new HashMap();
            paramRelated.put("goods_ids", relatedGoodsIds);
            paramRelated.put("fields", "id, name, list_pic_url,primary_pic_url,retail_price");
            relatedGoods = goodsService.queryList(paramRelated);
        }
        relatedGoods = relatedGoods.stream().limit(10).collect(Collectors.toList());
        resultObj.put("goodsList", relatedGoods);
        return toResponsSuccess(resultObj);
    }


    /**
     * 　　在售的商品总数
     */
    @ApiOperation(value = "在售的商品总数")
    @IgnoreAuth
    @GetMapping(value = "count")
    public Object count() {
        Map<String, Object> resultObj = new HashMap();
        Map param = new HashMap();
        param.put("is_delete", 0);
        param.put("is_on_sale", 1);
        Integer goodsCount = goodsService.queryTotal(param);
        resultObj.put("goodsCount", goodsCount);
        return toResponsSuccess(resultObj);
    }

    /**
     * 　　获取商品列表
     */
    @ApiOperation(value = "获取商品列表")
    @IgnoreAuth
    @GetMapping(value = "productlist")
    public Object productlist(Integer categoryId,
                              Integer isNew, Integer discount,
                              @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String sort, String order) {
        Map params = new HashMap();
        params.put("is_new", isNew);
        params.put("page", page);
        params.put("limit", size);
        params.put("order", sort);
        params.put("sidx", order);
        //
        if (null != sort && sort.equals("price")) {
            params.put("sidx", "retail_price");
            params.put("order", order);
        } else if (null != sort && sort.equals("sell")) {
            params.put("sidx", "orderNum");
            params.put("order", order);
        } else {
            params.put("sidx", "id");
            params.put("order", "desc");
        }
        // 0不限 1特价 2团购
        if (null != discount && discount == 1) {
            params.put("is_hot", 1);
        } else if (null != discount && discount == 2) {
            params.put("is_group", true);
        }
        //加入分类条件
        if (null != categoryId && categoryId > 0) {
            List<Integer> categoryIds = new ArrayList();
            Map categoryParam = new HashMap();
            categoryParam.put("parent_id", categoryId);
            categoryParam.put("fields", "id");
            List<CategoryVo> childIds = categoryService.queryList(categoryParam);
            for (CategoryVo categoryEntity : childIds) {
                categoryIds.add(categoryEntity.getId());
            }
            categoryIds.add(categoryId);
            params.put("categoryIds", categoryIds);
        }
        //查询列表数据
        Query query = new Query(params);
        List<GoodsVo> goodsList = goodsService.queryCatalogProductList(query);
        int total = goodsService.queryTotal(query);

        // 当前购物车中
        List<CartVo> cartList = new ArrayList();
        if (null != getUserId()) {
            //查询列表数据
            Map cartParam = new HashMap();
            cartParam.put("user_id", getUserId());
            cartList = cartService.queryList(cartParam);
        }
        if (null != cartList && cartList.size() > 0 && null != goodsList && goodsList.size() > 0) {
            for (GoodsVo goodsVo : goodsList) {
                for (CartVo cartVo : cartList) {
                    if (goodsVo.getId().equals(cartVo.getGoods_id())) {
                        goodsVo.setCart_num(cartVo.getNumber());
                    }
                }
            }
        }
        ApiPageUtils goodsData = new ApiPageUtils(goodsList, total, query.getLimit(), query.getPage());
        goodsData.setGoodsList(goodsData.getData());
        return toResponsSuccess(goodsData);
    }


    /**
     * 商品二维码图片请求
     */
    @ApiOperation(value = "商品二维码图片请求")
    @GetMapping("getGoodCode")
    public Object getGoodCode(@APPLoginUser MlsUserEntity2 loginUser, Integer goodId, HttpServletRequest request) {
        String urlPath = request.getServletContext().getRealPath(File.separator);//系统路径
        String pagePath = "statics" + File.separator + "goodQrCode" + File.separator + loginUser.getMlsUserId() + File.separator;//文件目录
        String qrCode = loginUser.getMlsUserId() + goodId + ".png";//文件名称
        String qrcodeUrl = urlPath + pagePath + qrCode;//全部路径
        String returnUrl = pagePath + qrCode;//返回路径
        //查看文件夹是否存在
        QRCodeUtils.dirExists(new File(urlPath + pagePath));
        //判断文件是否存在，存在就返回路径
        if (QRCodeUtils.fileExists(new File(qrcodeUrl))) {
            toResponsSuccess(returnUrl);
        }

        //底图
        String baseFilePath = urlPath + "statics/base/base.png";


        //获取商品主图和2张配图
        GoodsVo goods = goodsService.queryObject(goodId);
        String p1 = goods.getPrimary_pic_url();//主图
        String p2 = goods.getList_pic_url();//配图1
        String p3 = goods.getList_pic_url();//配图2

        //获取logo
        BrandVo brand = brandService.queryObject(goods.getBrand_id());
        String p4 = brand.getLogo();
        /**
         获取二维码图(调用小程序API)
         String accessToken = tokenService.getAccessToken() ;
         BufferedInputStream bis = QRCodeUtils.getGoodQrCode(accessToken,this.getUserId(), goodId);
         **/

        try {
            //非调用小程序生产二维码
            String content = "http://muserqrcode.51shop.ink?id=" + goodId + "&userId=" + loginUser.getMlsUserId();
            BufferedImage qrcode = QRCodeUtil.createImage(content, null, false);

            String newUrl = ImageUtils.coverImage(baseFilePath, p1, 34, 306, 645, 645, qrcodeUrl);
            ImageUtils.coverImage(newUrl, p2, 700, 306, 315, 315, qrcodeUrl);
            ImageUtils.coverImage(newUrl, p3, 700, 642, 315, 315, qrcodeUrl);
            ImageUtils.coverImage(newUrl, p4, 50, 50, 125, 125, qrcodeUrl);
            ImageUtils.coverImage(newUrl, qrcode, 48, 1025, 200, 200, qrcodeUrl);
            ImageUtils.coverText(newUrl, brand.getName(), 220, 100, qrcodeUrl, new Font("Courier", Font.BOLD, 40), Color.black);
            ImageUtils.coverText(newUrl, goods.getName(), 53, 240, qrcodeUrl, new Font("Courier", Font.BOLD, 40), Color.gray);
            ImageUtils.coverText(newUrl, "请在微信长按识别二维码选购商品", 280, 1180, qrcodeUrl, new Font("Courier", Font.BOLD, 22), Color.lightGray);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return toResponsSuccess(returnUrl);

    }


    /**
     * 商品二维码图片请求
     */
    @ApiOperation(value = "商品二维码图片请求")
    @GetMapping("getGoodCode2")
    public Object getGoodCode2(@APPLoginUser MlsUserEntity2 loginUser, Integer goodId) {
        String urlPath = request.getServletContext().getRealPath(File.separator);//系统路径
        String pagePath = "statics" + File.separator + "goodQrCode" + File.separator + loginUser.getMlsUserId() + File.separator;//文件目录
        String qrCode = loginUser.getMlsUserId() + goodId + "money.png";//文件名称
        String qrcodeUrl = urlPath + pagePath + qrCode;//全部路径
        String returnUrl = pagePath + qrCode;//返回路径
        //查看文件夹是否存在
        QRCodeUtils.dirExists(new File(urlPath + pagePath));
        //判断文件是否存在，存在就返回路径
        if (QRCodeUtils.fileExists(new File(qrcodeUrl))) {
            toResponsSuccess(returnUrl);
        }

        //底图
        String baseFilePath = urlPath + "statics/base/base2.png";

        //获取商品主图和2张配图
        GoodsVo goods = goodsService.queryObject(goodId);
        String p1 = goods.getPrimary_pic_url();//主图
        String p2 = goods.getList_pic_url();//配图1
        String p3 = goods.getList_pic_url();//配图2

        //获取logo
        BrandVo brand = brandService.queryObject(goods.getBrand_id());
        String p4 = brand.getLogo();
        /**
         获取二维码图(调用小程序API)
         String accessToken = tokenService.getAccessToken() ;
         BufferedInputStream bis = QRCodeUtils.getGoodQrCode(accessToken,this.getUserId(), goodId);
         **/

        try {
            //非调用小程序生产二维码
            String content = "http://muserqrcode.51shop.ink?id=" + goodId + "&userId=" + loginUser.getMlsUserId();
            BufferedImage qrcode = QRCodeUtil.createImage(content, null, false);

            String newUrl = ImageUtils.coverImage(baseFilePath, p1, 50, 423, 645, 645, qrcodeUrl);
            ImageUtils.coverImage(newUrl, p2, 700, 423, 315, 315, qrcodeUrl);
            ImageUtils.coverImage(newUrl, p3, 700, 755, 315, 315, qrcodeUrl);
            ImageUtils.coverImage(newUrl, p4, 50, 50, 130, 130, qrcodeUrl);
            ImageUtils.coverImage(newUrl, qrcode, 48, 1100, 200, 200, qrcodeUrl);
            ImageUtils.coverText(newUrl, brand.getName(), 220, 100, qrcodeUrl, new Font("宋体", Font.BOLD, 40), Color.black);
            ImageUtils.coverText(newUrl, goods.getName(), 53, 240, qrcodeUrl, new Font("宋体", Font.BOLD, 40), Color.gray);
            ImageUtils.coverText(newUrl, "￥", 50, 348, qrcodeUrl, new Font("宋体", Font.BOLD, 30), Color.RED);
            ImageUtils.coverText(newUrl, goods.getRetail_price().toString(), 90, 348, qrcodeUrl, new Font("宋体", Font.BOLD, 50), Color.RED);

            AttributedString as = new AttributedString(goods.getMarket_price().toString());
            as.addAttribute(TextAttribute.FONT, new Font("宋体", Font.BOLD, 30));
            as.addAttribute(TextAttribute.FOREGROUND, Color.lightGray);
            as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
            ImageUtils.coverText(newUrl, 320, 348, qrcodeUrl, as);
            ImageUtils.coverText(newUrl, "请在微信长按识别二维码选购商品", 280, 1275, qrcodeUrl, new Font("宋体", Font.BOLD, 26), Color.lightGray);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return toResponsSuccess(returnUrl);
    }


    /**
     * 上传商品图片
     */
    @ApiOperation(value = "上传商品图片")
    @RequestMapping("uploadImage")
    public String uploadImage(@RequestParam("file") MultipartFile file, String imageType) {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
        //上传文件
        String url = null;
        try {
            url = OSSFactory.build().upload(file);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return url;
    }

    /**
     * app新增商品
     */
    @ApiOperation(value = "app新增商品")
    @RequestMapping("appAdd")
    public Object appAdd(@RequestParam String goodsname, @RequestParam String imageUrl, @RequestParam String imageUrl2) {
        GoodsVo goods = new GoodsVo();
        Integer id = goodsService.queryMaxId() + 1;
        goods.setId(id);
        goods.setName(goodsname);
        goods.setPrimary_pic_url(imageUrl);
        goods.setList_pic_url(imageUrl2);
        goodsService.save(goods);

        return toResponsSuccess(goods);
    }
    /**
     *根据产品组合规则查询产品库存
     * @param goodsId
     * @param goods_spec
     * @return
     */
    @RequestMapping("/productSpecification")
    @IgnoreAuth
    public Object getProductByGooids(@RequestParam Integer goodsId,@RequestParam String goods_spec){

        if(goodsId == null || StringUtils.isNullOrEmpty(goods_spec)){
            return toResponsFail("参数不合法");
        }
        String[] str = goods_spec.split("_");
        List<String> list1 = Arrays.asList(str);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("goods_id",goodsId);
        List<ProductVo> productVos = apiProductService.queryList(hashMap);
        for (int i = 0; i < productVos.size(); i++) {
            String specificatione_ids = productVos.get(i).getGoods_specification_ids();
            List<String> list2 = Arrays.asList(specificatione_ids.split("_"));
            if(list1.containsAll(list2)){
                return toResponsSuccess( productVos.get(i));
            }
        }
        return toResponsFail(null);
    }

    public List<File> getFile() {
        return file;
    }

    public void setFile(List<File> file) {
        this.file = file;
    }

    public List<String> getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(List<String> fileFileName) {
        this.fileFileName = fileFileName;
    }

    public List<String> getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(List<String> fileContentType) {
        this.fileContentType = fileContentType;
    }

}