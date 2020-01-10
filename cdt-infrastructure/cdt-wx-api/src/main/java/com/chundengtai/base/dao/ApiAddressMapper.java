package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.AddressVo;

import java.util.List;
import java.util.Map;

/**
 */
public interface ApiAddressMapper extends BaseBizMapper<AddressVo> {

    void updateIsDefault(AddressVo addressVo);

    List<AddressVo> queryaddressUserlist(Map<String, Object> param);

    List<AddressVo> queryAddressCustomerlist(Map<String, Object> param);

    AddressVo queryDefaultAddress(Long userId);
}
