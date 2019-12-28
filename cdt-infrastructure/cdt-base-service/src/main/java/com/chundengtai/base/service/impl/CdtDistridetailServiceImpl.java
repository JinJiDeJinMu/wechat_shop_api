package com.chundengtai.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chundengtai.base.bean.CdtDistridetail;
import com.chundengtai.base.dao.CdtDistridetailMapper;
import com.chundengtai.base.service.CdtDistridetailService;
import com.chundengtai.base.utils.ReflectUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户购买分销得钱记录 服务实现类
 * </p>
 *
 * @author Royal
 * @since 2019-12-18
 */
@Service
public class CdtDistridetailServiceImpl extends ServiceImpl<CdtDistridetailMapper, CdtDistridetail> implements CdtDistridetailService {

    @Override
    public BigDecimal getUnsetMoney(Wrapper wrapper) {
        return this.getUnsetMoney(wrapper);
    }

    @Override
    public BigDecimal getTotalMoney(Wrapper wrapper) {
        return this.getTotalMoney(wrapper);
    }

    /**
     * 新增一条用户购买分销得钱记录
     *
     * @param cdtDistridetail
     * @return
     */
    @Override
    public Boolean saveCdtDistridetail(CdtDistridetail cdtDistridetail) {
        cdtDistridetail.setToken(ReflectUtils.getToken(cdtDistridetail));
        return this.save(cdtDistridetail);
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<CdtDistridetail> queryList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return this.list();
    }
}
