package com.chundengtai.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chundengtai.base.bean.CdtUserDistribution;
import com.chundengtai.base.dao.CdtUserDistributionMapper;
import com.chundengtai.base.service.CdtUserDistributionService;
import com.chundengtai.base.tool.ReflectUtils;
import com.platform.page.PageHelper;
import com.platform.utils.ShiroUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户分布 服务实现类
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
@Service
public class CdtUserDistributionServiceImpl extends ServiceImpl<CdtUserDistributionMapper, CdtUserDistribution> implements CdtUserDistributionService {

    @Override
    public List<CdtUserDistribution> queryList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return this.list();
    }

    @Override
    public Boolean addCdtUserDistribution(CdtUserDistribution cdtUserDistribution) {
        cdtUserDistribution.setCreatedTime(new Date());
        cdtUserDistribution.setCreatedBy(String.valueOf(ShiroUtils.getUserEntity().getUserId()));
        cdtUserDistribution.setToken(ReflectUtils.getToken(cdtUserDistribution));
        return this.save(cdtUserDistribution);
    }
}
