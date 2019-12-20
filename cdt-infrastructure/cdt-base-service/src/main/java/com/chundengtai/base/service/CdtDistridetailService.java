package com.chundengtai.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chundengtai.base.bean.CdtDistridetail;

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

    Boolean saveCdtDistridetail(CdtDistridetail cdtDistridetail);

    List<CdtDistridetail> queryList(Integer pageNum, Integer pageSize);

}
