package com.chundengtai.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chundengtai.base.bean.CdtRebateLog;
import com.chundengtai.base.dao.CdtRebateLogMapper;
import com.chundengtai.base.service.CdtRebateLogService;
import com.chundengtai.base.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 分销记录表 服务实现类
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
@Service
public class CdtRebateLogServiceImpl extends ServiceImpl<CdtRebateLogMapper, CdtRebateLog> implements CdtRebateLogService {

    @Override
    public List<CdtRebateLog> queryList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return this.list();
    }

    @Override
    public Boolean addCdtRebateLog(CdtRebateLog cdtRebateLog) {
        cdtRebateLog.setToken(ReflectUtils.getToken(cdtRebateLog));
        return this.save(cdtRebateLog);
    }
}
