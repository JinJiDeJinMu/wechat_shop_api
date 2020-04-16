package com.chundengtai.base.service;

import com.alibaba.fastjson.JSONObject;
import com.chundengtai.base.bean.CdtPostageTemplate;
import com.chundengtai.base.constant.CacheConstant;
import com.chundengtai.base.dao.*;
import com.chundengtai.base.entity.*;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import com.chundengtai.base.weixinapi.OrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
@Slf4j
public class ApiOrderService {
    @Autowired
    private ApiOrderMapper orderDao;
    @Autowired
    private ApiAddressMapper apiAddressMapper;
    @Autowired
    private ApiCartMapper apiCartMapper;
    @Autowired
    private ApiOrderMapper apiOrderMapper;
    @Autowired
    private ApiOrderGoodsMapper apiOrderGoodsMapper;
    @Autowired
    private ApiProductService productService;
    @Autowired
    private ApiGoodsService goodsService;
    @Autowired
    UserRecordSer userRecordSer;
    @Autowired
    private ApiGoodsSpecificationService goodsSpecificationService;
    @Autowired
    private ApiExpressOrderService apiExpressOrderService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private KeygenService keygenService;

    @Autowired
    private CdtPostageTemplateService cdtPostageTemplateService;

    public OrderVo queryObject(Integer id) {
        return orderDao.queryObject(id);
    }

    public List<OrderVo> queryPageList(Map<String, Object> map) {
        return orderDao.queryPageList(map);
    }

    public List<OrderVo> queryList(Map<String, Object> map) {
        return orderDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return orderDao.queryTotal(map);
    }

    public void save(OrderVo order) {
        orderDao.save(order);
    }

    public int update(OrderVo order) {
        return orderDao.update(order);
    }

    public void delete(Integer id) {
        orderDao.delete(id);
    }

    public void deleteBatch(Integer[] ids) {
        orderDao.deleteBatch(ids);
    }

    @Transactional
    public Map<String, Object> submit(JSONObject jsonParam, UserVo loginUser) {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        // 判断优惠券是否属于个人（防止个人通过接口请求）
        String couponIds = jsonParam.getString(" ");
        // payType 1：团购操作，2：秒杀，不传为普通购买
        String payType = jsonParam.getString("payType");
        //团购小组的ID，如果创建团购则id为空
        String groupBuyingId = jsonParam.getString("groupBuyingId");

        String type = jsonParam.getString("type");//提交方式
        String postscript = jsonParam.getString("postscript");//留言
        Long promoterId = jsonParam.getLong("promoterId");// 获取推荐人id
        log.info("=====================获取推荐人id:" + promoterId);


        AddressVo addressVo = apiAddressMapper.queryObject(jsonParam.getInteger("addressId"));//收货地址

        //需要一个总订单ID,付款的时候计算全部价格
        //String all_order_id = UUID.randomUUID().toString().replaceAll("-", "");
        String allOrderId = "pay" + String.valueOf(keygenService.genNumber().longValue());
        if (type.equals("cart")) {//购物车提交
            //查询所有购物车根据供应商分类
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("user_id" , loginUser.getUserId());
            List<CartVo> mrchantList = apiCartMapper.queryMrchantGroup(param);
            //不同供应商产品集合
            List<List<CartVo>> newMrchantList = new ArrayList<List<CartVo>>();
            //根据不同供应商查询购物车商品
            for (CartVo vo : mrchantList) {
                param.put("merchant_id", vo.getMerchant_id());
                param.put("checked", 1);
                newMrchantList.add(apiCartMapper.queryList(param));
            }
            //每个供应商ID形成一个订单
            BigDecimal goodsTotalPrice;//商品总额
            //循环供应商订单
            for (List<CartVo> checkedGoodsList : newMrchantList) {
                if (null == checkedGoodsList) {
                    resultObj.put("errno", 400);
                    resultObj.put("errmsg", "请选择商品");
                    return resultObj;
                }
                BigDecimal brokerage_price = BigDecimal.ZERO;//总返利
                BigDecimal freightPrice = BigDecimal.ZERO;//运费
                Integer couponId = null;//优惠券ID
                BigDecimal couponPrice = BigDecimal.ZERO;//优惠券金额
                goodsTotalPrice = BigDecimal.ZERO;//商品总额
                Long merchant_id = null;//供应商ID
                //循环供应商中商品信息
                for (CartVo cartItem : checkedGoodsList) {
                    //计算商品价格
                    goodsTotalPrice = goodsTotalPrice
                            .add(cartItem.getRetail_price().multiply(new BigDecimal(cartItem.getNumber())));
                    ProductVo productInfo = productService.queryObject(cartItem.getProduct_id());
                    System.out.println("库存===="+productInfo);
                    //判断库存
                    if (null == productInfo || productInfo.getGoods_number() < cartItem.getNumber()) {
                        resultObj.put("errno", 500);
                        resultObj.put("errmsg", cartItem.getGoods_name() + "库存不足");
                        return resultObj;
                    } else {
                        productInfo.setGoods_number(productInfo.getGoods_number() - cartItem.getNumber());
                        productInfo.setSale_number(productInfo.getSale_number() + cartItem.getNumber());
                        productService.update(productInfo);
                        if(productInfo.getGoods_number() ==0){
                            GoodsVo goodsVo = goodsService.queryObject(productInfo.getGoods_id());
                            goodsVo.setGoods_number(0);
                            goodsVo.setIs_on_sale(0);
                            goodsService.update(goodsVo);
                        }
                    }
                    // 运费统计
                    Integer goodId = cartItem.getGoods_id();
                    GoodsVo goods = goodsService.queryObject(goodId);
                   /* if (goods.getExtra_price() != null) {
                        //todo:运费修复
                        //freightPrice = freightPrice
                        //        .add(goods.getExtra_price().multiply(new BigDecimal(cartItem.getNumber())));

                        freightPrice = freightPrice
                                .add(goods.getExtra_price().multiply(new BigDecimal(1)));
                    }*/
                    freightPrice = freightPrice.add(getPostageMoney(goodId,cartItem.getNumber()));
                    //计算反润金额(返利 + 返利比例 * 商品金额 * 商品数量)
                    brokerage_price = brokerage_price.add(new BigDecimal(goods.getBrokerage_percent())
                            .multiply(goods.getRetail_price()).multiply(new BigDecimal(cartItem.getNumber())));

                    //记录一下供应商ID
                    if (merchant_id == null) {
                        merchant_id = cartItem.getMerchant_id();
                    }
                }

                OrderVo orderInfo = getOrderVo(loginUser, postscript, addressVo, allOrderId, goodsTotalPrice, freightPrice, couponId, couponPrice);
                // 加入推荐人
                if (promoterId != null) {
                    orderInfo.setPromoter_id(promoterId.intValue());
                }
                // 计算佣金
                orderInfo.setBrokerage(brokerage_price);
                // 供应商ID
                orderInfo.setMerchant_id(merchant_id);
                orderInfo.setOrder_type("1");

                int goodsNum = Optional
                        .ofNullable(checkedGoodsList.size())
                        .orElse(0);
                orderInfo.setGoods_num(goodsNum);
                // 保存订单信息
                apiOrderMapper.save(orderInfo);
                // 循环订单商品表
                for (CartVo goodsItem : checkedGoodsList) {
                    OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
                    orderGoodsVo.setOrder_id(orderInfo.getId());
                    orderGoodsVo.setGoods_id(goodsItem.getGoods_id());
                    orderGoodsVo.setGoods_sn(goodsItem.getGoods_sn());
                    orderGoodsVo.setProduct_id(goodsItem.getProduct_id());
                    orderGoodsVo.setGoods_name(goodsItem.getGoods_name());

                    orderGoodsVo.setList_pic_url(goodsItem.getList_pic_url());
                    orderGoodsVo.setMarket_price(goodsItem.getMarket_price());
                    orderGoodsVo.setRetail_price(goodsItem.getRetail_price());
                    orderGoodsVo.setNumber(goodsItem.getNumber());
                    //保存商品规格等信息
                    orderGoodsVo.setGoods_specifition_ids(goodsItem.getGoods_specifition_ids());//规格属性id集合
                    orderGoodsVo.setGoods_specifition_name_value(goodsItem.getGoods_specifition_name_value());//规格属性集合
                    // 保存订单商品表
                    apiOrderGoodsMapper.save(orderGoodsVo);
                }

                // 返回对象
                Map<String, OrderVo> orderInfoMap = new HashMap<String, OrderVo>();
                orderInfoMap.put("orderInfo", orderInfo);
                resultObj.put("errno", 0);
                resultObj.put("errmsg", "订单提交成功");
                resultObj.put("data", orderInfoMap);
            }

        } else {//直接购买
            if (StringUtils.isBlank(payType) || "2".equals(payType)) {//普通和秒杀
                BigDecimal freightPrice = BigDecimal.ZERO;//运费
                Integer couponId = null;
                BigDecimal couponPrice = BigDecimal.ZERO;//优惠券金额

                BuyGoodsVo goodsVo = (BuyGoodsVo) redisTemplate.opsForValue().get(CacheConstant.SHOP_GOODS_CACHE + loginUser.getUserId());
                if (null == goodsVo) {
                    resultObj.put("errno", 400);
                    resultObj.put("errmsg", "请选择商品");
                    return resultObj;
                }

                // 商品规格信息
                ProductVo productInfo = productService.queryObject(goodsVo.getProductId());
                // 商品总价
                BigDecimal goodsTotalPrice = productInfo.getRetail_price().multiply(new BigDecimal(goodsVo.getNumber()));

                //判断库存
                if (null == productInfo || productInfo.getGoods_number() < goodsVo.getNumber()) {
                    resultObj.put("errno", 500);
                    resultObj.put("errmsg", productInfo.getGoods_name() + "库存不足");
                    return resultObj;
                } else {
                    productInfo.setGoods_number(productInfo.getGoods_number() - goodsVo.getNumber());
                    productInfo.setSale_number(productInfo.getSale_number() + goodsVo.getNumber());
                    productService.update(productInfo);
                    if(productInfo.getGoods_number() ==0){
                        GoodsVo goodsVo1 = goodsService.queryObject(productInfo.getGoods_id());
                        goodsVo1.setGoods_number(0);
                        goodsVo1.setIs_on_sale(0);
                        goodsService.update(goodsVo1);
                    }
                }
                // 运费统计
                Integer goodId = goodsVo.getGoodsId();
                GoodsVo goods = goodsService.queryObject(goodId);
               /* if (goods.getExtra_price() != null) {
                    freightPrice = freightPrice
                            .add(goods.getExtra_price().multiply(new BigDecimal(1)));
                }*/
                freightPrice = getPostageMoney(goodId,goodsVo.getNumber());
                //计算反润金额(返利 + 返利比例 * 商品金额 * 商品数量)
                BigDecimal brokerage_price = goods.getRetail_price()
                        .multiply(new BigDecimal(goods.getBrokerage_percent() * goodsVo.getNumber())).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);

                // 订单价格计算
                OrderVo orderInfo = getOrderVo(loginUser, postscript, addressVo, allOrderId, goodsTotalPrice, freightPrice, couponId, couponPrice);
                // 加入推荐人
                if (promoterId != null) {
                    orderInfo.setPromoter_id(promoterId.intValue());
                }
                // 计算佣金
                orderInfo.setBrokerage(brokerage_price);
                // 供应商ID
                orderInfo.setMerchant_id(goods.getMerchantId());
                orderInfo.setOrder_type(payType == null ? "2" : "3");

                //todo:集成核销  下单直接购买
                orderInfo.setGoods_type(goods.getIs_secKill());
                orderInfo.setGoods_num(1);
                // 保存订单信息
                apiOrderMapper.save(orderInfo);

                // 保存订单商品表
                OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
                orderGoodsVo.setOrder_id(orderInfo.getId());
                orderGoodsVo.setGoods_id(productInfo.getGoods_id());
                orderGoodsVo.setGoods_sn(productInfo.getGoods_sn());
                orderGoodsVo.setProduct_id(goodsVo.getProductId());
                orderGoodsVo.setGoods_name(productInfo.getGoods_name());
                if (StringUtils.isEmpty(productInfo.getList_pic_url())) {
                    orderGoodsVo.setList_pic_url(goods.getPrimary_pic_url());
                } else {
                    orderGoodsVo.setList_pic_url(productInfo.getList_pic_url());
                }
                orderGoodsVo.setMarket_price(productInfo.getMarket_price());
                orderGoodsVo.setRetail_price(productInfo.getRetail_price());
                orderGoodsVo.setNumber(goodsVo.getNumber());
                orderGoodsVo.setCoupon_id(couponId);
                //保存商品规格等信息
                orderGoodsVo.setGoods_specifition_ids(productInfo.getGoods_specification_ids());
                //添加规格名和值
                String[] goodsSepcifitionValue = null;
                if (null != productInfo.getGoods_specification_ids() && productInfo.getGoods_specification_ids().length() > 0) {
                    Map specificationParam = new HashMap();
                    String[] idsArray = getSpecificationIdsArray(productInfo.getGoods_specification_ids());
                    specificationParam.put("ids", idsArray);
                    specificationParam.put("goods_id", productInfo.getGoods_id());
                    List<GoodsSpecificationVo> specificationEntities = goodsSpecificationService.queryList(specificationParam);
                    goodsSepcifitionValue = new String[specificationEntities.size()];
                    for (int i = 0; i < specificationEntities.size(); i++) {
                        goodsSepcifitionValue[i] = specificationEntities.get(i).getValue();
                    }
                }
                //规格属性集合
                if (null != goodsSepcifitionValue) {
                    orderGoodsVo.setGoods_specifition_name_value(StringUtils.join(goodsSepcifitionValue, ";"));
                }
                apiOrderGoodsMapper.save(orderGoodsVo);

                if (goodsVo.getGoodsType().equals(GoodsTypeEnum.EXPRESS_GET.getCode())) {
                    ExpressOrderVo expressOrderVo = (ExpressOrderVo) redisTemplate.opsForValue().get(CacheConstant.EXPRESS_GOODS_CACHE + loginUser.getUserId());
                    //保存快递订单表
                    expressOrderVo.setUserId(loginUser.getUserId().intValue());
                    expressOrderVo.setOrderId(orderInfo.getId().toString());
                    apiExpressOrderService.save(expressOrderVo);
                }

                // 返回对象
                Map<String, OrderVo> orderInfoMap = new HashMap<String, OrderVo>();
                orderInfoMap.put("orderInfo", orderInfo);
                resultObj.put("errno", 0);
                resultObj.put("errmsg", "订单提交成功");
                resultObj.put("data", orderInfoMap);
            }
            //团购购买
            else {
                BigDecimal freightPrice = BigDecimal.ZERO;//运费
                Integer couponId = null;
                BigDecimal couponPrice = BigDecimal.ZERO;//优惠券金额

                BuyGoodsVo goodsVo = (BuyGoodsVo) redisTemplate.opsForValue().get(CacheConstant.SHOP_GOODS_CACHE + loginUser.getUserId());
                if (null == goodsVo) {
                    resultObj.put("errno", 400);
                    resultObj.put("errmsg", "请选择商品");
                    return resultObj;
                }
                // 商品规格信息
                ProductVo productInfo = productService.queryObject(goodsVo.getProductId());
                GoodsVo goods = goodsService.queryObject(goodsVo.getGoodsId());
                // 商品总价
                BigDecimal goodsTotalPrice = productInfo.getGroup_price().multiply(new BigDecimal(goodsVo.getNumber()));

                //判断库存（团购不判断库存）
				/*if (null == productInfo || productInfo.getGoods_number() < goodsVo.getNumber()) {
					resultObj.put("errno", 500);
					resultObj.put("errmsg", productInfo.getGoods_name() + "库存不足");
					return resultObj;
				} else {
					productInfo.setGoods_number(productInfo.getGoods_number() - goodsVo.getNumber());
					productService.update(productInfo);
				}*/
                // 运费统计(团购免运费)
				/*if (goods.getExtra_price() != null) {
					freightPrice = freightPrice
							.add(goods.getExtra_price().multiply(new BigDecimal(goodsVo.getNumber())));
				}*/
                //计算反润金额(返利 + 返利比例 * 商品金额 * 商品数量)(团购不返利)
				/*BigDecimal brokerage_price = goods.getRetail_price()
						.multiply(new BigDecimal(goods.getBrokerage_percent() * goodsVo.getNumber())).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
				*/

                // 订单价格计算
                OrderVo orderInfo = getOrderVo(loginUser, postscript, addressVo, allOrderId, goodsTotalPrice, freightPrice, couponId, couponPrice);
                // 加入推荐人
                if (promoterId != null) {
                    orderInfo.setPromoter_id(promoterId.intValue());
                }

                // 供应商ID
                orderInfo.setMerchant_id(goods.getMerchantId());
                if (StringUtils.isBlank(groupBuyingId)) {
                    groupBuyingId = UUID.randomUUID().toString().replaceAll("-", "");
                }
                orderInfo.setGroup_buying_id(groupBuyingId);
                orderInfo.setOrder_type("4");
                // 保存订单信息
                apiOrderMapper.save(orderInfo);
                // 保存订单商品表
                OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
                orderGoodsVo.setOrder_id(orderInfo.getId());
                orderGoodsVo.setGoods_id(productInfo.getGoods_id());
                orderGoodsVo.setGoods_sn(productInfo.getGoods_sn());
                orderGoodsVo.setProduct_id(goodsVo.getProductId());
                orderGoodsVo.setGoods_name(productInfo.getGoods_name());
                orderGoodsVo.setList_pic_url(productInfo.getList_pic_url());
                orderGoodsVo.setMarket_price(productInfo.getMarket_price());
                orderGoodsVo.setRetail_price(productInfo.getRetail_price());
                orderGoodsVo.setNumber(goodsVo.getNumber());
                orderGoodsVo.setCoupon_id(couponId);
                //保存规格信息
                //保存商品规格等信息
                orderGoodsVo.setGoods_specifition_ids(productInfo.getGoods_specification_ids());//规格属性id集合
                //添加规格名和值
                String[] goodsSepcifitionValue = null;
                if (null != productInfo.getGoods_specification_ids() && productInfo.getGoods_specification_ids().length() > 0) {
                    Map specificationParam = new HashMap();
                    String[] idsArray = getSpecificationIdsArray(productInfo.getGoods_specification_ids());
                    specificationParam.put("ids", idsArray);
                    specificationParam.put("goods_id", productInfo.getGoods_id());
                    List<GoodsSpecificationVo> specificationEntities = goodsSpecificationService.queryList(specificationParam);
                    goodsSepcifitionValue = new String[specificationEntities.size()];
                    for (int i = 0; i < specificationEntities.size(); i++) {
                        goodsSepcifitionValue[i] = specificationEntities.get(i).getValue();
                    }
                }
                //规格属性集合
                if (null != goodsSepcifitionValue) {
                    orderGoodsVo.setGoods_specifition_name_value(StringUtils.join(goodsSepcifitionValue, ";"));
                }

                apiOrderGoodsMapper.save(orderGoodsVo);
                // 返回对象
                Map<String, OrderVo> orderInfoMap = new HashMap<String, OrderVo>();
                orderInfoMap.put("orderInfo", orderInfo);
                resultObj.put("errno", 0);
                resultObj.put("errmsg", "订单提交成功");
                resultObj.put("data", orderInfoMap);
            }

        }
        return resultObj;
    }

    private OrderVo getOrderVo(UserVo loginUser, String postscript, AddressVo addressVo, String all_order_id, BigDecimal goodsTotalPrice, BigDecimal freightPrice, Integer couponId, BigDecimal couponPrice) {
        // 订单价格计算
        BigDecimal orderTotalPrice = goodsTotalPrice.add(freightPrice); // 订单的总价
        BigDecimal actualPrice = orderTotalPrice.subtract(couponPrice); // 减去其它支付的金额后，要实际支付的金额
        OrderVo orderInfo = new OrderVo();
        // 总订单编号
        orderInfo.setAll_order_id(all_order_id);
        //orderInfo.setOrder_sn(CommonUtil.generateOrderNumber());
        orderInfo.setOrder_sn(String.valueOf(keygenService.genNumber().longValue()));
        orderInfo.setUser_id(loginUser.getUserId());
        // 收货地址和运费
        orderInfo.setConsignee(addressVo.getUserName());
        orderInfo.setMobile(addressVo.getTelNumber());
        orderInfo.setCountry(addressVo.getNationalCode());
        orderInfo.setProvince(addressVo.getProvinceName());
        orderInfo.setCity(addressVo.getCityName());
        orderInfo.setDistrict(addressVo.getCountyName());
        orderInfo.setAddress(addressVo.getDetailInfo());
        // 留言
        orderInfo.setPostscript(postscript);
        // 使用的优惠券
        orderInfo.setCoupon_id(couponId);
        orderInfo.setCoupon_price(couponPrice);
        // 订单金额
        orderInfo.setAdd_time(new Date());
        orderInfo.setGoods_price(goodsTotalPrice);
        orderInfo.setOrder_price(orderTotalPrice);
        orderInfo.setActual_price(actualPrice);
        orderInfo.setAll_price(actualPrice);
        orderInfo.setFreight_price(freightPrice);
        // 设置为待付款状态
        orderInfo.setOrder_status(OrderStatusEnum.WAIT_PAY.getCode());
        orderInfo.setShipping_status(0);
        orderInfo.setPay_status(0);
        orderInfo.setShipping_id(0);
        orderInfo.setShipping_fee(freightPrice);
        orderInfo.setIntegral(0);
        orderInfo.setIntegral_money(new BigDecimal(0));

        //todo:绑定一二级分销
        orderInfo.setFirstLeader(loginUser.getFirstLeader());
        orderInfo.setSecondLeader(loginUser.getSecondLeader());
        return orderInfo;
    }

    private String[] getSpecificationIdsArray(String ids) {
        String[] idsArray = null;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(ids)) {
            String[] tempArray = ids.split("_");
            if (null != tempArray && tempArray.length > 0) {
                idsArray = tempArray;
            }
        }
        return idsArray;
    }

    public void updateStatus(OrderVo vo) {
        apiOrderMapper.updateStatus(vo);
    }

    public List<OrderVo> queryWaitList() {
        return apiOrderMapper.queryWaitList();
    }

    public List<OrderVo> queryFxList() {
        return apiOrderMapper.queryFxList();
    }

    public List<OrderVo> queryByAllOrderId(String all_order_id) {
        return apiOrderMapper.queryByAllOrderId(all_order_id);
    }

    public OrderVo queryOrderNo(String order_sn) {
        return apiOrderMapper.queryOrderNo(order_sn);
    }

    public OrderVo queryByOrderId(String allOrderId) {
        return apiOrderMapper.queryByOrderId(allOrderId);
    }

    public List<OrderVo> queryGroupBuyRefundList(Map map) {
        return apiOrderMapper.queryGroupBuyRefundList(map);
    }

    public void cancelFx(int orderId, Date payTime, int orderPrice) {
        UserRecord entity = new UserRecord();
        entity.setOrderId(orderId);
        List<UserRecord> userRecordList = userRecordSer.getEntityMapper().findByEntity(entity);
        for (UserRecord ur : userRecordList) {
            UserRecord newur = new UserRecord();
            newur.setMlsUserId(ur.getMlsUserId());
            newur.setTypes(3);
            newur.setTypesStr("退回分润");
            newur.setPrice(ur.getPrice());
            newur.setRemarks("退回:" + ur.getRemarks());
            newur.setOrderId(orderId);
            userRecordSer.save(newur);
        }
    }

    //计算邮费
    public BigDecimal getPostageMoney(Integer goodsId,Integer number){

        GoodsVo goodsVo = goodsService.queryObject(goodsId);
        if(goodsVo == null || GoodsTypeEnum.EXPRESS_GET.getCode().equals(goodsVo.getIs_secKill())){
            return new BigDecimal(BigInteger.ZERO);
        }
        if(goodsVo.getExpressType() == 0){
            return goodsVo.getExtra_price();
        }

        //获取模板信息
        CdtPostageTemplate cdtPostageTemplate = cdtPostageTemplateService.getById(goodsVo.getExpressType());
        if(cdtPostageTemplate == null){
            return goodsVo.getExtra_price().multiply(new BigDecimal(number));
        }
        BigDecimal money = cdtPostageTemplate.getMoney();
        Integer first = cdtPostageTemplate.getFirst();
        Integer second = cdtPostageTemplate.getSecond();
        //如果小于首个数量
        if(number <= first || second == 0){
            return money;
        }
        //获取超出首个的邮费
        Integer exter = number - first;
        Integer count = exter%second == 0 ? (exter/second) : (exter/second)+1;
        BigDecimal money_exter = new BigDecimal(count).multiply(cdtPostageTemplate.getRenewMoney());

        return money.add(money_exter);
    }

}
