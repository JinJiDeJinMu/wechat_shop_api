package com.chundengtai.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chundengtai.base.bean.CdtRebateLog;

import java.util.List;

/**
 * <p>
 * 分销记录表 服务类
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
public interface CdtRebateLogService extends IService<CdtRebateLog> {

    List<CdtRebateLog> queryList(Integer pageNum, Integer pageSize);

    Boolean addCdtRebateLog(CdtRebateLog cdtRebateLog);

}
