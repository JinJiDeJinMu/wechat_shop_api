package com.platform.service;


import com.platform.dao.ApiRepCommentMapper;
import com.platform.entity.RepCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 回复评论表Service接口
 *
 * @author lipengjun
 * @date 2019-11-18 19:35:15
 */
@Service
public class ApiRepCommentService {

    @Autowired
    private ApiRepCommentMapper apiRepCommentMapper;

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    public RepCommentVo queryObject(Integer id) {
        return apiRepCommentMapper.queryObject(id);
    }

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    public List<RepCommentVo> queryList(Map<String, Object> map) {
        return apiRepCommentMapper.queryList(map);
    }

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    public int queryTotal(Map<String, Object> map) {
        return apiRepCommentMapper.queryTotal(map);
    }

    /**
     * 保存实体
     *
     * @param repComment 实体
     * @return 保存条数
     */
    public int save(RepCommentVo repComment) {
        return apiRepCommentMapper.save(repComment);
    }

    /**
     * 根据主键更新实体
     *
     * @param repComment 实体
     * @return 更新条数
     */
    public int update(RepCommentVo repComment) {
        return apiRepCommentMapper.update(repComment);
    }

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    public int delete(Integer id) {
        return apiRepCommentMapper.delete(id);
    }

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    public int deleteBatch(Integer[] ids) {
        return apiRepCommentMapper.deleteBatch(ids);
    }
}
