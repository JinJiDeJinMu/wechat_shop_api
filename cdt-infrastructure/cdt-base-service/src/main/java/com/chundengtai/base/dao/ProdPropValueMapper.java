
package com.chundengtai.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chundengtai.base.bean.ProdPropValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProdPropValueMapper extends BaseMapper<ProdPropValue> {

    void insertPropValues(@Param("propId") Long propId, @Param("prodPropValues") List<ProdPropValue> prodPropValues);

    void deleteByPropId(@Param("propId") Long propId);
}