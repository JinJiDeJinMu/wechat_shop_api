package com.chundengtai.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chundengtai.base.bean.CdtDistributionLevel;
import com.chundengtai.base.dao.CdtDistributionLevelMapper;
import com.chundengtai.base.service.CdtDistributionLevelService;
import com.chundengtai.base.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 分销层级表 分销层级表-绑定用户关系 服务实现类
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
@Service
public class CdtDistributionLevelServiceImpl extends ServiceImpl<CdtDistributionLevelMapper, CdtDistributionLevel> implements CdtDistributionLevelService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<CdtDistributionLevel> queryList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return this.list();
    }

    /**
     * 新增一条分销层级记录
     *
     * @param cdtDistributionLevel
     * @return
     */
    @Override
    public Boolean addDistributionLevel(CdtDistributionLevel cdtDistributionLevel) {
        cdtDistributionLevel.setCreatedTime(new Date());
        cdtDistributionLevel.setToken(ReflectUtils.getToken(cdtDistributionLevel));
        return this.save(cdtDistributionLevel);
    }

    /**
     * 修改一条分销层级记录
     *
     * @param cdtDistributionLevel
     * @return
     */
    @Override
    public Boolean updateDistributionLevel(CdtDistributionLevel cdtDistributionLevel) {
        Date date = this.getById(cdtDistributionLevel.getId()).getCreatedTime();
        cdtDistributionLevel.setCreatedTime(date);
        cdtDistributionLevel.setToken(ReflectUtils.getToken(cdtDistributionLevel));
        return this.updateById(cdtDistributionLevel);
    }

}
