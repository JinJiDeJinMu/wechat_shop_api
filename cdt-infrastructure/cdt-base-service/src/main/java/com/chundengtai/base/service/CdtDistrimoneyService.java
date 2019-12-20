package com.chundengtai.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chundengtai.base.bean.CdtDistrimoney;

import java.util.List;

/**
 * <p>
 * 分销比例 服务类
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
public interface CdtDistrimoneyService extends IService<CdtDistrimoney> {

    List<CdtDistrimoney> queryList(Integer pageNum, Integer pageSize);

    Boolean addCdtDistrimoney(CdtDistrimoney cdtDistrimoney);

    Boolean updateDistrimoney(CdtDistrimoney cdtDistrimoney);

}
