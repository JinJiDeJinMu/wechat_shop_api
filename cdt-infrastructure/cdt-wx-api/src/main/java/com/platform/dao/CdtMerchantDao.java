package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.CdtMerchantEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 商家Dao
 *
 * @date 2019-11-15 17:08:05
 */
public interface CdtMerchantDao extends BaseBizMapper<CdtMerchantEntity> {


    CdtMerchantEntity queryByGoodsId(@Param("goodsId") Integer goodsId);

    int add(CdtMerchantEntity cdtMerchantEntity);

}
