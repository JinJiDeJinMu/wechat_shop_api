package com.chundengtai.base.server.dao;

import com.chundengtai.base.server.common.BaseBizMapper;
import com.platform.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 系统配置信息
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2016年12月4日 下午6:46:16
 */
public interface ConfigDao extends BaseBizMapper<SysConfigEntity> {

    /**
     * 根据key，查询value
     */
    String queryByKey(String paramKey);

    /**
     * 根据key，更新value
     *
     * @param key
     * @param value
     * @return
     */
    int updateValueByKey(@Param("key") String key, @Param("value") String value);

}