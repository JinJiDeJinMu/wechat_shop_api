package com.chundengtai.base.service.admin;

import com.chundengtai.base.entity.UserRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouhaisheng on 2019/3/2.
 */

public interface UserRecordService {
    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<UserRecordEntity> queryList(Map<String, Object> map);

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    int queryTotal(Map<String, Object> map);
}
