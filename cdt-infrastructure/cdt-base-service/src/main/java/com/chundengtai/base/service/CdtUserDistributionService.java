package com.chundengtai.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chundengtai.base.bean.CdtUserDistribution;

import java.util.List;

/**
 * <p>
 * 用户分布 服务类
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
public interface CdtUserDistributionService extends IService<CdtUserDistribution> {

    List<CdtUserDistribution> queryList(Integer pageNum, Integer pageSize);

    Boolean addCdtUserDistribution(CdtUserDistribution cdtUserDistribution);

}
