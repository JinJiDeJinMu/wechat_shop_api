package com.platform.service;

import com.platform.entity.CommentV2Entity;

import java.util.List;
import java.util.Map;


/**
 * 评价表Service接口
 *
 * @date 2019-11-26 11:27:34
 */
public interface CommentV2Service {

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    CommentV2Entity queryObject(Integer id);

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    List<CommentV2Entity> queryList(Map<String, Object> map);

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    int queryTotal(Map<String, Object> map);

    /**
     * 保存实体
     *
     * @param nideshopCommentV2 实体
     * @return 保存条数
     */
    int save(CommentV2Entity nideshopCommentV2);

    /**
     * 根据主键更新实体
     *
     * @param nideshopCommentV2 实体
     * @return 更新条数
     */
    int update(CommentV2Entity nideshopCommentV2);

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    int delete(Integer id);

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    int deleteBatch(Integer[] ids);

    int toggleStatus(CommentV2Entity comment);
}
