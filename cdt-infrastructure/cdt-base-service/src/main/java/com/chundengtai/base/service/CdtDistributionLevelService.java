package com.chundengtai.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chundengtai.base.bean.CdtDistributionLevel;

import java.util.List;

/**
 * <p>
 * 分销层级表 分销层级表-绑定用户关系 服务类
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
public interface CdtDistributionLevelService extends IService<CdtDistributionLevel> {

    List<CdtDistributionLevel> queryList(Integer pageNum, Integer pageSize);

    Boolean addDistributionLevel(CdtDistributionLevel cdtDistributionLevel);

    Boolean updateDistributionLevel(CdtDistributionLevel cdtDistributionLevel);

}
