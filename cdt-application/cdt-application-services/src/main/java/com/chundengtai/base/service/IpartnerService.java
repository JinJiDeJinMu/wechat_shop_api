package com.chundengtai.base.service;

import com.chundengtai.base.bean.CdtDistrimoney;
import com.chundengtai.base.bean.CdtUserSummary;

/**
 * **************************************************************
 * 公司名称    :
 * 系统名称    :有机屋
 * 类 名 称    :
 * 功能描述    :
 * 业务描述    :
 * 作 者 名    :@Author amtf
 * 开发日期    :2020/1/2 下午2:32
 * Created    :IntelliJ IDEA
 * **************************************************************
 * 修改日期    :
 * 修 改 者    :
 * 修改内容    :
 * **************************************************************
 */
public interface IpartnerService {
    CdtUserSummary getPartnerInfo(CdtDistrimoney distrimoney, int userId);

    void determinePartner(CdtDistrimoney distrimoney, int userId);
}
