package com.platform.dao;

import com.platform.entity.CdtMerchantEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 商家Dao
 *
 * @author lipengjun
 * @date 2019-11-15 17:08:05
 */
public interface CdtMerchantDao extends BaseDao<CdtMerchantEntity> {


    CdtMerchantEntity queryByGoodsId(@Param("goodsId") Integer goodsId);

}
