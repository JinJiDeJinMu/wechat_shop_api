package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.AddressVo;

import java.util.List;
import java.util.Map;

/**
 * @date 2017-08-11 09:14:25
 */
public interface ApiAddressMapper extends BaseBizMapper<AddressVo> {

    void updateIsDefault(AddressVo addressVo);

    List<AddressVo> queryaddressUserlist(Map<String, Object> param);

    List<AddressVo> queryAddressCustomerlist(Map<String, Object> param);

    AddressVo queryDefaultAddress(Long userId);
}
