package com.chundengtai.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chundengtai.base.bean.CdtDistridetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户购买分销得钱记录 服务类
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
public interface CdtDistridetailService extends IService<CdtDistridetail> {

    BigDecimal getUnsetMoney(Wrapper wrapper);

    BigDecimal getTotalMoney(Wrapper wrapper);

    Boolean saveCdtDistridetail(CdtDistridetail cdtDistridetail);

    List<CdtDistridetail> queryList(Integer pageNum, Integer pageSize);

}
