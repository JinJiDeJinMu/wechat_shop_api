package com.chundengtai.base.server.dao;


import com.chundengtai.base.server.common.BaseBizMapper;
import com.chundengtai.base.server.entity.GroupBuyingVo;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author xuyang
 * @email 295640759@qq.com
 * @date 2019-06-13 22:00:12
 */
public interface GroupBuyingMapper extends BaseBizMapper<GroupBuyingVo> {

    List<GroupBuyingVo> queryLoseList(Map map);
}
