package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.GoodsSpecificationDao;
import com.chundengtai.base.dao.ProductDao;
import com.chundengtai.base.entity.GoodsSpecificationEntity;
import com.chundengtai.base.entity.ProductEntity;
import com.chundengtai.base.service.admin.ProductService;
import com.chundengtai.base.utils.BeanUtils;
import com.chundengtai.base.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private GoodsSpecificationDao goodsSpecificationDao;

    @Override
    public ProductEntity queryObject(Integer id) {
        return productDao.queryObject(id);
    }

    @Override
    public List<ProductEntity> queryList(Map<String, Object> map) {
        List<ProductEntity> list = productDao.queryList(map);

        List<ProductEntity> result = new ArrayList<>();
        //翻译产品规格
        if (null != list && list.size() > 0) {
            for (ProductEntity item : list) {
                String specificationIds = item.getGoodsSpecificationIds();
                String specificationValue = "";
                if (!StringUtils.isNullOrEmpty(specificationIds)) {
                    String[] arr = specificationIds.split("_");

                    for (String goodsSpecificationId : arr) {
                        GoodsSpecificationEntity entity = goodsSpecificationDao.queryObject(goodsSpecificationId);
                        if (null != entity) {
                            specificationValue += entity.getValue() + "；";
                        }
                    }
                }
                item.setSpecificationValue(item.getGoodsName() + " " + specificationValue);
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return productDao.queryTotal(map);
    }

    @Override
    @Transactional
    public int save(ProductEntity product) {
        int result = 0;
        String goodsSpecificationIds = product.getGoodsSpecificationIds();
        if (!StringUtils.isNullOrEmpty(goodsSpecificationIds) && !goodsSpecificationIds.equals("_")) {
            String[] goodsSpecificationIdArr = goodsSpecificationIds.split("_");
            if (goodsSpecificationIdArr.length == 2) {
                for (int i = 0; i < goodsSpecificationIdArr.length - 1; i++) {
                    String[] oneId = goodsSpecificationIdArr[i].split(",");
                    String[] twoId = goodsSpecificationIdArr[i + 1].split(",");
                    for (int j = 0; j < oneId.length; j++) {
                        for (int k = 0; k < twoId.length; k++) {
                            String strGoodsSpecificationIds = null;
                            if (StringUtils.isNullOrEmpty(oneId[j]) || StringUtils.isNullOrEmpty(twoId[k])) {
                                continue;
                            }
                            strGoodsSpecificationIds = oneId[j] + "_" + twoId[k];
                            product.setGoodsSpecificationIds(strGoodsSpecificationIds);
                            ProductEntity entity = new ProductEntity();
                            BeanUtils.copyProperties(product, entity);
                            result += productDao.save(entity);
                        }
                    }
                }
            } else if (goodsSpecificationIdArr.length == 1) {
                String goodsSpecificationId[] = goodsSpecificationIdArr[0].split(",");
                for (int i = 0; i < goodsSpecificationId.length; i++) {
                    String string = goodsSpecificationId[i];
                    String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
                    product.setGoodsSpecificationIds(string.replaceAll(regEx,""));
                    ProductEntity entity = new ProductEntity();
                    BeanUtils.copyProperties(product, entity);
                    result += productDao.save(entity);
                }
            }

        } else {
            System.out.println("规格没有，");
            if (product.getGoodsSpecificationIds().equals("_")) {
                product.setGoodsSpecificationIds("");
            }
            result += productDao.save(product);
        }
        return result;
    }


    @Override
    @Transactional
    public int saveOne(ProductEntity product) {
        int result = 0;
        String goodsSpecificationIds = product.getGoodsSpecificationIds();
        if (!StringUtils.isNullOrEmpty(goodsSpecificationIds)) {
            String[] goodsSpecificationIdArr = goodsSpecificationIds.split("_");
            for (int i = 0; i < goodsSpecificationIdArr.length - 1; i++) {
                String[] oneId = goodsSpecificationIdArr[i].split(",");
                String[] twoId = goodsSpecificationIdArr[i + 1].split(",");
                for (int j = 0; j < oneId.length; j++) {
                    for (int k = 0; k < twoId.length; k++) {
                        String strGoodsSpecificationIds = null;
                        if (StringUtils.isNullOrEmpty(oneId[j]) || StringUtils.isNullOrEmpty(twoId[k])) {
                            continue;
                        }
                        strGoodsSpecificationIds = oneId[j] + "_" + twoId[k];
                        product.setGoodsSpecificationIds(strGoodsSpecificationIds);
                        ProductEntity entity = new ProductEntity();
                        BeanUtils.copyProperties(product, entity);
                        result += productDao.save(entity);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public int update(ProductEntity product) {
        if (StringUtils.isNullOrEmpty(product.getGoodsSpecificationIds())) {
            product.setGoodsSpecificationIds("");
        }
        return productDao.update(product);
    }

    @Override
    public int delete(Integer id) {
        return productDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return productDao.deleteBatch(ids);
    }

    public static void main(String[] args) {
        String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";

        String str = "[123]";
        System.out.println(str.replaceAll(regEx,""));
    }
}
