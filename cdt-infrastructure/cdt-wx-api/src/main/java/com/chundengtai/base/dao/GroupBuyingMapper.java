package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.GroupBuyingVo;

import java.util.List;
import java.util.Map;

public interface GroupBuyingMapper extends BaseBizMapper<GroupBuyingVo> {

    List<GroupBuyingVo> queryLoseList(Map map);
}
