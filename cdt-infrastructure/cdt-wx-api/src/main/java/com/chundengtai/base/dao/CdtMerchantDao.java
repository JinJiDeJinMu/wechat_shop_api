package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.CdtMerchantEntity;
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
