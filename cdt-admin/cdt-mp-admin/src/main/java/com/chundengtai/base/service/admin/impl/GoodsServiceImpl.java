package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.GoodsAttributeDao;
import com.chundengtai.base.dao.GoodsDao;
import com.chundengtai.base.dao.GoodsGalleryDao;
import com.chundengtai.base.dao.ProductDao;
import com.chundengtai.base.entity.GoodsAttributeEntity;
import com.chundengtai.base.entity.GoodsEntity;
import com.chundengtai.base.entity.GoodsGalleryEntity;
import com.chundengtai.base.entity.ProductEntity;
import com.chundengtai.base.service.admin.GoodsService;
import com.chundengtai.base.utils.RRException;
import com.chundengtai.base.utils.ShiroUtils;
import com.chundengtai.base.weixinapi.GoodsTypeEnum;
import com.platform.entity.SysUserEntity;
import com.platform.service.KeygenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @date 2017-08-21 21:19:49
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsAttributeDao goodsAttributeDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private GoodsGalleryDao goodsGalleryDao;

    @Autowired
    private KeygenService keygenService;

    @Override
    public GoodsEntity queryObject(Integer id) {
        return goodsDao.queryObject(id);
    }

    @Override
    //@DataFilter(userAlias = "nideshop_goods.create_user_id", deptAlias = "nideshop_goods.create_user_dept_id")
    public List<GoodsEntity> queryList(Map<String, Object> map) {
        return goodsDao.queryList(map);
    }

    @Override
    //@DataFilter(userAlias = "nideshop_goods.create_user_id", deptAlias = "nideshop_goods.create_user_dept_id")
    public int queryTotal(Map<String, Object> map) {
        return goodsDao.queryTotal(map);
    }

    @Override
    @Transactional
    public int save(GoodsEntity goods) {
        SysUserEntity user = ShiroUtils.getUserEntity();
        Map<String, Object> map = new HashMap<>();
        map.put("name", goods.getName());
        List<GoodsEntity> list = queryList(map);
        if (null != list && list.size() != 0) {
            throw new RRException("商品名称已存在！");
        }
        //Integer id = Integer.valueOf(keygenService.genNumber().shortValue());
        Integer id = 1;
        try {
            id = goodsDao.queryMaxId() + 1;

        } catch (Exception ex) {
            goods.setId(1);
        }
        goods.setId(id);
        //保存产品信息
        ProductEntity productEntity = new ProductEntity();
        productEntity.setGoodsId(id);
        productEntity.setGoodsSn(goods.getGoodsSn());
        productEntity.setGoodsNumber(goods.getGoodsNumber());
        productEntity.setRetailPrice(goods.getRetailPrice());
        productEntity.setMarketPrice(goods.getMarketPrice());
        if (StringUtils.isEmpty(goods.getListPicUrl())) {
            productEntity.setPicUrl(goods.getPrimaryPicUrl());
        } else {
            productEntity.setPicUrl(goods.getListPicUrl());
        }
        productEntity.setGoodsSpecificationIds("");

        if (goods.getMerchantId() != null) {
            productEntity.setMerchant_id(goods.getMerchantId());
        }
        productEntity.setGroupPrice(goods.getGroupPrice());
        productDao.save(productEntity);

        goods.setAddTime(new Date());
        goods.setPrimaryProductId(productEntity.getId());

        //保存商品详情页面显示的属性
        List<GoodsAttributeEntity> attributeEntityList = goods.getAttributeEntityList();
        if (null != attributeEntityList && attributeEntityList.size() > 0) {
            for (GoodsAttributeEntity item : attributeEntityList) {
                item.setGoodsId(id);
                goodsAttributeDao.save(item);
            }
        }

        //商品轮播图
        List<GoodsGalleryEntity> galleryEntityList = goods.getGoodsImgList();
        if (null != galleryEntityList && galleryEntityList.size() > 0) {
            for (GoodsGalleryEntity galleryEntity : galleryEntityList) {
                galleryEntity.setGoodsId(id);
                goodsGalleryDao.save(galleryEntity);
            }
        }

        goods.setIsDelete(0);
        goods.setCreateUserDeptId(user.getDeptId());
        goods.setCreateUserId(user.getUserId());
        goods.setUpdateUserId(user.getUserId());
        goods.setUpdateTime(new Date());
        if (goods.getIsSecKill().equals(GoodsTypeEnum.EXPRESS_GET.getCode()) || (goods.getIsSecKill().equals(GoodsTypeEnum.WRITEOFF_ORDER.getCode()))) {
            goods.setExtraPrice(BigDecimal.valueOf(0));
        }
        return goodsDao.save(goods);
    }

    @Override
    @Transactional
    public int update(GoodsEntity goods) {
        SysUserEntity user = ShiroUtils.getUserEntity();
        List<GoodsAttributeEntity> attributeEntityList = goods.getAttributeEntityList();
        if (null != attributeEntityList && attributeEntityList.size() > 0) {
            for (GoodsAttributeEntity goodsAttributeEntity : attributeEntityList) {
                int result = goodsAttributeDao.updateByGoodsIdAttributeId(goodsAttributeEntity);
                if (result == 0) {
                    goodsAttributeDao.save(goodsAttributeEntity);
                }
            }
        }
        goods.setUpdateUserId(user.getUserId());
        goods.setUpdateTime(new Date());
        //商品轮播图
        //修改时全删全插
        List<GoodsGalleryEntity> galleryEntityList = goods.getGoodsImgList();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("goodsId", goods.getId());
        goodsGalleryDao.deleteByGoodsId(map);

        if (null != galleryEntityList && galleryEntityList.size() > 0) {
            for (GoodsGalleryEntity galleryEntity : galleryEntityList) {
                galleryEntity.setGoodsId(goods.getId());
                goodsGalleryDao.save(galleryEntity);
            }
        }
        return goodsDao.update(goods);
    }

    @Override
    public int delete(Integer id) {
        SysUserEntity user = ShiroUtils.getUserEntity();
        GoodsEntity goodsEntity = queryObject(id);
        goodsEntity.setIsDelete(1);
        goodsEntity.setIsOnSale(0);
        goodsEntity.setUpdateUserId(user.getUserId());
        goodsEntity.setUpdateTime(new Date());
        return goodsDao.update(goodsEntity);
    }

    @Override
    @Transactional
    public int deleteBatch(Integer[] ids) {
        int result = 0;
        for (Integer id : ids) {
            result += delete(id);
        }
        return result;
    }

    @Override
    @Transactional
    public int back(Integer[] ids) {
        SysUserEntity user = ShiroUtils.getUserEntity();
        int result = 0;
        for (Integer id : ids) {
            GoodsEntity goodsEntity = queryObject(id);
            goodsEntity.setIsDelete(0);
            goodsEntity.setIsOnSale(1);
            goodsEntity.setUpdateUserId(user.getUserId());
            goodsEntity.setUpdateTime(new Date());
            result += goodsDao.update(goodsEntity);
        }
        return result;
    }

    @Override
    public int enSale(Integer id) {
        SysUserEntity user = ShiroUtils.getUserEntity();
        GoodsEntity goodsEntity = queryObject(id);
        if (1 == goodsEntity.getIsOnSale()) {
            throw new RRException("此商品已处于上架状态！");
        }
        goodsEntity.setIsOnSale(1);
        goodsEntity.setUpdateUserId(user.getUserId());
        goodsEntity.setUpdateTime(new Date());
        return goodsDao.update(goodsEntity);
    }

    @Override
    public int unSale(Integer id) {
        SysUserEntity user = ShiroUtils.getUserEntity();
        GoodsEntity goodsEntity = queryObject(id);
        if (0 == goodsEntity.getIsOnSale()) {
            throw new RRException("此商品已处于下架状态！");
        }
        goodsEntity.setIsOnSale(0);
        goodsEntity.setUpdateUserId(user.getUserId());
        goodsEntity.setUpdateTime(new Date());
        return goodsDao.update(goodsEntity);
    }
}
