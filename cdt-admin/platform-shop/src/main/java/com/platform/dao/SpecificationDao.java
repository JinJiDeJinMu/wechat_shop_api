package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.SpecificationEntity;

import java.util.List;

/**
 * 规格表
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-13 10:41:10
 */
public interface SpecificationDao extends BaseBizMapper<SpecificationEntity> {
	List<SpecificationEntity> queryListByGoodsId(String goodsId);
	
}
