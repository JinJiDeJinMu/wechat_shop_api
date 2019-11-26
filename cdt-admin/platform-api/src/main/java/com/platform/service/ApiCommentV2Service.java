package com.platform.service;

import com.platform.entity.CommentReq;
import com.platform.dao.ApiCommentV2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiCommentV2Service {
    @Autowired
    private ApiCommentV2Mapper apiCommentV2Mapper;


    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
    public CommentReq queryObject(Integer id) {
        return apiCommentV2Mapper.queryObject(id);
    }

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
    public List<CommentReq> queryList(Map<String, Object> map) {
        return apiCommentV2Mapper.queryList(map);
    }

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
    public int queryTotal(Map<String, Object> map) {
        return apiCommentV2Mapper.queryTotal(map);
    }

    /**
     * 保存实体
     *
     * @param commentReq 实体
     * @return 保存条数
     */
    public int save(CommentReq commentReq) {
        return apiCommentV2Mapper.save(commentReq);
    }

    /**
     * 根据主键更新实体
     *
     * @param commentReq 实体
     * @return 更新条数
     */
    public int update(CommentReq commentReq) {
        return apiCommentV2Mapper.update(commentReq);
    }

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
    public int delete(Integer id) {
        return apiCommentV2Mapper.delete(id);
    }

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
    public int deleteBatch(Integer[] ids) {
        return apiCommentV2Mapper.deleteBatch(ids);
    }
}
