package com.chundengtai.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chundengtai.base.bean.CdtDistrimoney;
import com.chundengtai.base.dao.CdtDistrimoneyMapper;
import com.chundengtai.base.service.CdtDistrimoneyService;
import com.chundengtai.base.tool.ReflectUtils;
import com.github.pagehelper.PageHelper;
import com.platform.utils.ShiroUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 分销比例 服务实现类
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
@Service
public class CdtDistrimoneyServiceImpl extends ServiceImpl<CdtDistrimoneyMapper, CdtDistrimoney> implements CdtDistrimoneyService {

    @Override
    public List<CdtDistrimoney> queryList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return this.list();
    }

    @Override
    public Boolean addCdtDistrimoney(CdtDistrimoney cdtDistrimoney) {
        cdtDistrimoney.setCreatedTime(new Date());
        cdtDistrimoney.setCreatedBy(String.valueOf(ShiroUtils.getUserEntity().getUserId()));
        cdtDistrimoney.setToken(ReflectUtils.getToken(cdtDistrimoney));
        return this.save(cdtDistrimoney);
    }

    @Override
    public Boolean updateDistrimoney(CdtDistrimoney cdtDistrimoney) {
        cdtDistrimoney.setUpdatedBy(String.valueOf(ShiroUtils.getUserEntity().getUserId()));
        cdtDistrimoney.setUpdatedTime(new Date());
        cdtDistrimoney.setToken(ReflectUtils.getToken(cdtDistrimoney));
        return this.updateById(cdtDistrimoney);
    }


}
