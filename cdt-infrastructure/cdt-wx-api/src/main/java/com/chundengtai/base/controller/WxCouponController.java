package com.chundengtai.base.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chundengtai.base.annotation.IgnoreAuth;
import com.chundengtai.base.annotation.LoginUser;
import com.chundengtai.base.bean.CdtCoupon;
import com.chundengtai.base.bean.CdtCouponGoods;
import com.chundengtai.base.bean.CdtUserCoupon;
import com.chundengtai.base.bean.Goods;
import com.chundengtai.base.bean.dto.CdtUserCouponDao;
import com.chundengtai.base.constant.CacheConstant;
import com.chundengtai.base.dao.CdtUserCouponMapper;
import com.chundengtai.base.entity.BuyGoodsVo;
import com.chundengtai.base.entity.ProductVo;
import com.chundengtai.base.entity.UserVo;
import com.chundengtai.base.result.Result;
import com.chundengtai.base.service.*;
import com.chundengtai.base.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 优惠券
 * @Author hujinbiao
 * @Date 2020年3月17日 0017 下午 04:18:20
 * @Version 1.0
 **/
@Slf4j
@Api(tags = "优惠券相关")
@RestController
@RequestMapping("/apis/v2/coupon")
public class WxCouponController extends ApiBaseAction {

    @Autowired
    private CdtCouponGoodsService couponGoodsService;

    @Autowired
    private CdtCouponService couponService;

    @Autowired
    private CdtUserCouponService userCouponService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private ApiProductService apiProductService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CdtUserCouponMapper cdtUserCouponMapper;

    /**
     * 获取用户优惠券列表
     * @param
     * @return
     */
    @ApiOperation(value = "获取用户优惠券列表")
    @GetMapping("/userCouponList.json")
    @IgnoreAuth
    public Result getUserCouponList(/*@LoginUser UserVo loginUser*/){

        Map<String,Object> params = new HashMap<>(4);
        params.put("userId",135);
        List<CdtUserCouponDao> cdtUserCouponDaoList = userCouponService.getUserCounponList(params);
        cdtUserCouponDaoList.forEach(e->{
            checkUserCouponStatus(e);
        });
        return Result.success(cdtUserCouponDaoList);
    }


    /**
     * 获取商家店铺优惠券列表
     * @param merchantId
     * @return
     */
    @ApiOperation(value = "获取商家店铺优惠券列表")
    @GetMapping("/merchantCouponList.json")
    public Result getMerchantCouponList(Integer merchantId){

        List<CdtCoupon> cdtCouponList = couponService.list(new LambdaQueryWrapper<CdtCoupon>()
                .eq(CdtCoupon::getMerchantId,merchantId));
        return Result.success(cdtCouponList);
    }


    @ApiOperation(value = "用户点击领劵")
    @PostMapping("/coupon.do")
    public Result doUserCoupon(@LoginUser UserVo userVo,Integer couponId){
        //先判断是否领过
        CdtCoupon cdtCoupon = couponService.getById(couponId);
        if(cdtCoupon == null){
            return Result.failure("优惠券不存在");
        }
        CdtUserCoupon cdtUserCoupon = userCouponService.getOne(new LambdaQueryWrapper<CdtUserCoupon>()
                .eq(CdtUserCoupon::getCouponId,couponId).eq(CdtUserCoupon::getUserId,userVo.getUserId()));

        if(cdtUserCoupon != null){
            return Result.failure("优惠券已领取");
        }
        //判断优惠券是否被领取完
        if(cdtCoupon.getReceiveCount() >= cdtCoupon.getTotalCount()){
            return Result.failure("优惠券已经被领取完");
        }
        //会员判断

        CdtUserCoupon userCoupon = new CdtUserCoupon();
        userCoupon.setCouponId(couponId);
        userCoupon.setUserId(userVo.getUserId().intValue());
        userCoupon.setNumber(1);
        userCoupon.setUserTime(new Date());
        //事务更新领取和减少优惠券数量
        userCouponService.doUserCoupon(userCoupon,cdtCoupon);

        return Result.success();
    }

    /**
     * 查询店铺指定商品可以领取的优惠券
     * @param merchantId
     * @return
     */
    @ApiOperation(value = "查询店铺指定商品可以领取的优惠券")
    @GetMapping("/getGoodCoupon.json")
    @IgnoreAuth
    public Result getGoodCoupon(Integer merchantId){

        //查询可领店铺优惠券
        List<CdtCoupon> cdtCouponList = couponService.list(new LambdaQueryWrapper<CdtCoupon>()
                .eq(CdtCoupon::getMerchantId,merchantId));

        cdtCouponList = cdtCouponList.stream().filter(e->e.getTotalCount() > e.getReceiveCount())
                .collect(Collectors.toList());
        Iterator<CdtCoupon> iterator = cdtCouponList.iterator();
        while(iterator.hasNext()){
            CdtCoupon coupon = iterator.next();
            if(coupon.getTimeType() == 0){
                if(coupon.getEndDate().before(new Date())){
                   iterator.remove();
                }
            }
        }
        return Result.success(cdtCouponList);
    }


    /**
     * 查询用户满足条件的优惠券
     * @return
     */
    @ApiOperation(value = "查询用户满足条件的优惠券")
    @GetMapping("ucoupon.json")
    @IgnoreAuth
   public Result getUserCoupon(/*@LoginUser UserVo userVo*/){

       BuyGoodsVo goodsVo = (BuyGoodsVo) redisTemplate.opsForValue().get(CacheConstant.SHOP_GOODS_CACHE + 135);
       ProductVo productVo = apiProductService.queryObject(goodsVo.getProductId());
       if(productVo == null){
           return Result.failure("产品不存在");
       }
       Goods goods = goodsService.getById(productVo.getGoods_id());
       if(goods == null || goods.getMerchantId() == null){
           return Result.failure("商品不存在");
       }

       Map<String, Object> hashmap = new HashMap<>(4);
       hashmap.put("userId",135);
       hashmap.put("merchantId",goods.getMerchantId());

       List<CdtUserCouponDao> userCouponDaoList = userCouponService.getUserCounponList(hashmap);
        //检查优惠券是否失效，失效的设置状态
       userCouponDaoList.forEach(e ->{ checkUserCouponStatus(e); });

       //去掉不符合条件的优惠券
       userCouponDaoList = userCouponDaoList.stream().filter(e->e.getStatus() == 0).collect(Collectors.toList());
       Iterator<CdtUserCouponDao> iterator = userCouponDaoList.iterator();
       while(iterator.hasNext()){
           CdtUserCouponDao coupon = iterator.next();
           Boolean flage = getCouponMoney(coupon,productVo.getRetail_price(),productVo.getGoods_id(),goods.getCategoryId());
           if(!flage){
               iterator.remove();
           }
       }

       return Result.success(userCouponDaoList);
   }
    /**
     * 判断用户优惠券是否失效状态
     * @param cdtUserCouponDao
     */
    public void checkUserCouponStatus(CdtUserCouponDao cdtUserCouponDao){
        if(cdtUserCouponDao.getStatus() ==0 ){
            Integer timeType = cdtUserCouponDao.getTimeType();
            Date userTime = cdtUserCouponDao.getUserTime();
            Date now = new Date();
            if(timeType == 0){
                Date endTime = cdtUserCouponDao.getEndDate();
                if(endTime.before(now)){
                    cdtUserCouponDao.setStatus(2);
                    CdtUserCoupon cdtUserCoupon = new CdtUserCoupon();
                    cdtUserCoupon.setUserId(cdtUserCouponDao.getUserId());
                    cdtUserCoupon.setCouponId(cdtUserCouponDao.getCouponId());
                    cdtUserCoupon.setNumber(cdtUserCouponDao.getNumber());
                    cdtUserCoupon.setUserTime(cdtUserCouponDao.getUserTime());
                    cdtUserCoupon.setStatus(2);
                    cdtUserCouponMapper.updateStatus(cdtUserCoupon);

                }
            }else if(timeType == 1){
                Calendar time = Calendar.getInstance();
                time.setTime(userTime);
                time.set(Calendar.DATE, time.get(Calendar.DATE) + cdtUserCouponDao.getDays());
                if(time.getTime().before(now)){
                    CdtUserCoupon cdtUserCoupon = new CdtUserCoupon();
                    cdtUserCoupon.setUserId(cdtUserCouponDao.getUserId());
                    cdtUserCoupon.setCouponId(cdtUserCouponDao.getCouponId());
                    cdtUserCoupon.setNumber(cdtUserCouponDao.getNumber());
                    cdtUserCoupon.setUserTime(cdtUserCouponDao.getUserTime());
                    cdtUserCoupon.setStatus(2);
                    cdtUserCouponMapper.updateStatus(cdtUserCoupon);
                }
            }
        }
    }


    /**
     * 判断符合条件的优惠券
     * @param cdtUserCouponDao
     * @param money
     * @param goodsId
     * @param category
     * @return
     */
    public Boolean getCouponMoney(CdtUserCouponDao cdtUserCouponDao,BigDecimal money,Integer goodsId,Integer category){

        Integer userType = cdtUserCouponDao.getUseType();
        Integer type = cdtUserCouponDao.getType();
        BigDecimal fullMoney = cdtUserCouponDao.getFullMoney();

        //全场适用
        if(userType == 0){
            return getMoney(type,money,fullMoney);

        }//指定分类
        else if(userType == 1){
          //查询适用商品信息
            CdtCouponGoods cdtCouponGoods = couponGoodsService.getOne(new LambdaQueryWrapper<CdtCouponGoods>()
                    .eq(CdtCouponGoods::getCouponId,cdtUserCouponDao.getCouponId()));
            if(cdtCouponGoods == null){
                return false;
            }
            if(category.equals(cdtCouponGoods.getCategoryId())){

                return getMoney(type,money,fullMoney);
            }
            return false;

        }//指定商品
        else if(userType == 2){
            CdtCouponGoods cdtCouponGoods = couponGoodsService.getOne(new LambdaQueryWrapper<CdtCouponGoods>()
                    .eq(CdtCouponGoods::getCouponId,cdtUserCouponDao.getCouponId()));
            if(cdtCouponGoods == null){
                return false;
            }

            String goods = cdtCouponGoods.getGoodsId();
            List<String> goodsList = Arrays.asList(goods.split(","));
            if(goodsList.contains(goodsId.toString())){
                return getMoney(type,money,fullMoney);
            }
            return false;
        }
        return false;
    }

    public Boolean getMoney(Integer type,BigDecimal money,BigDecimal fullMoney){

        return type == 0?money.compareTo(fullMoney) == 1:true;
    }
}
