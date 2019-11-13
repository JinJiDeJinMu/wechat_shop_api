package com.chundengtai.base.server.dao;


import com.chundengtai.base.server.common.BaseBizMapper;
import com.chundengtai.base.server.entity.AddressVo;
import java.util.List;
import java.util.Map;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:14:25
 */
public interface ApiAddressMapper extends BaseBizMapper<AddressVo> {

    void updateIsDefault(AddressVo addressVo);

    List<AddressVo> queryaddressUserlist(Map<String, Object> param);

    List<AddressVo> queryAddressCustomerlist(Map<String, Object> param);

    AddressVo queryDefaultAddress(Long userId);
}
