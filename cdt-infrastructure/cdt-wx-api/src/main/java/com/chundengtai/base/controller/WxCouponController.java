package com.chundengtai.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    public Result getUserCouponList(@LoginUser UserVo loginUser, @RequestParam(value = "status", defaultValue = "1") Integer status){

        Map<String,Object> params = new HashMap<>(4);
        params.put("userId",loginUser.getUserId());
        params.put("status",status);
        System.out.println("param===="+params);
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
    @IgnoreAuth
    public Result getMerchantCouponList(Integer merchantId){

        List<CdtCoupon> cdtCouponList = (List<CdtCoupon>) redisTemplate.opsForValue().get("CouponList_"+merchantId);
        if(cdtCouponList == null || cdtCouponList.size() ==0) {
            try {
                if (redisTemplate.opsForValue().setIfAbsent("coupon_" + merchantId,merchantId)) {
                    redisTemplate.expire("coupon_" + merchantId,2000,TimeUnit.MILLISECONDS);
                    cdtCouponList = couponService.list(new LambdaQueryWrapper<CdtCoupon>()
                            .eq(CdtCoupon::getMerchantId, merchantId));
                    redisTemplate.opsForValue().set("CouponList_" + merchantId, cdtCouponList, 5, TimeUnit.MINUTES);

                }
            }finally {
                redisTemplate.delete("coupon_" + merchantId);
            }

        }
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
        Date now = new Date();
        userCoupon.setUserTime(now);

        if(cdtCoupon.getTimeType() == 1){
            Calendar time = Calendar.getInstance();
            time.setTime(now);
            time.set(Calendar.DATE, time.get(Calendar.DATE) + cdtCoupon.getDays());
            userCoupon.setEndTime(time.getTime());
        }
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
   public Result getUserCoupon(@LoginUser UserVo userVo){

       BuyGoodsVo goodsVo = (BuyGoodsVo) redisTemplate.opsForValue().get(CacheConstant.SHOP_GOODS_CACHE + userVo.getUserId());
       ProductVo productVo = apiProductService.queryObject(goodsVo.getProductId());
       if(productVo == null){
           return Result.failure("产品不存在");
       }
       Goods goods = goodsService.getById(productVo.getGoods_id());
       if(goods == null || goods.getMerchantId() == null){
           return Result.failure("商品不存在");
       }

       Map<String, Object> hashmap = new HashMap<>(4);
       hashmap.put("userId",userVo.getUserId());
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
     * 根据用户选择的优惠券计算出优惠的金额
     * @param userVo
     * @param couponId
     * @return
     */
    @ApiOperation(value = "根据用户选择的优惠券计算出优惠的金额")
    @GetMapping("/getCouponMoney")
   public Result getCouponMoney(@LoginUser UserVo userVo,Integer couponId){
        if(couponId == null){
            return Result.failure("优惠券ID为空");
        }
        Map<String, Object> hashmap = new HashMap<>(4);
        hashmap.put("userId", userVo.getUserId());
        hashmap.put("couponId", couponId);
        List<CdtUserCouponDao> cdtUserCouponDaoList = userCouponService.getUserCounponList(hashmap);
        if(cdtUserCouponDaoList == null || cdtUserCouponDaoList.size()>=2){
            return Result.failure("用户优惠券不存在");
        }
        CdtUserCouponDao cdtUserCouponDao = cdtUserCouponDaoList.get(0);
        //再一次检查优惠券是否可以用状态
        checkUserCouponStatus(cdtUserCouponDao);
        if(cdtUserCouponDao.getStatus() != 0){
            return Result.failure("用户优惠券已失效");
        }
        BigDecimal money = checkMoney(userVo,cdtUserCouponDao);
        return Result.success(money);

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
                Date endTime = cdtUserCouponDao.getEndTime();
                if(endTime.before(now)){
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

    public BigDecimal checkMoney(UserVo userVo, CdtUserCouponDao cdtUserCouponDao){

        //查询用户购买产品的价格
        BuyGoodsVo goodsVo = (BuyGoodsVo) redisTemplate.opsForValue().get(CacheConstant.SHOP_GOODS_CACHE + userVo.getUserId());
        ProductVo productVo = apiProductService.queryObject(goodsVo.getProductId());
        BigDecimal money = productVo.getRetail_price();

        BigDecimal result = new BigDecimal(BigInteger.ZERO);
        Integer type = cdtUserCouponDao.getType();
        if(type == 0){
            result = money.subtract(cdtUserCouponDao.getReduceMoney()).intValue()>0?money.subtract(cdtUserCouponDao.getReduceMoney()):money;
        }else if(type == 1){
            result = money.multiply(cdtUserCouponDao.getDiscount());
        }else if(type == 2){
            result = money.subtract(cdtUserCouponDao.getOffsetMoney()).intValue()>0?money.subtract(cdtUserCouponDao.getOffsetMoney()):money;
        }
        return result;
    }

    public boolean lock(String key){
        String lock = key;
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + 1000 + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());


            if (acquire) {
                return true;
            } else {

                byte[] value = connection.get(lock.getBytes());

                if (Objects.nonNull(value) && value.length > 0) {

                    long expireTime = Long.parseLong(new String(value));
                    // 如果锁已经过期
                    if (expireTime < System.currentTimeMillis()) {
                        // 重新加锁，防止死锁
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + 1000 + 1).getBytes());
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }
}
