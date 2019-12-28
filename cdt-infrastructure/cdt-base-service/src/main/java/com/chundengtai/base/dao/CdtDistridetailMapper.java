package com.chundengtai.base.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.chundengtai.base.bean.CdtDistridetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 * 用户购买分销得钱记录 Mapper 接口
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
public interface CdtDistridetailMapper extends BaseMapper<CdtDistridetail> {

    @Select("select sum(money) from cdt_distridetail ${ew.customSqlSegment}")
    BigDecimal getUnsetMoney(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select sum(money) from cdt_distridetail ${ew.customSqlSegment}")
    BigDecimal getTotalMoney(@Param(Constants.WRAPPER) Wrapper wrapper);
}
