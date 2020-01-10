package com.platform.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.platform.entity.SysDeptEntity;
import com.platform.entity.UserWindowDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 部门管理
 *
 */
@Mapper
public interface SysDeptDao extends BaseBizMapper<SysDeptEntity> {

    /**
     * 查询子部门ID列表
     *
     * @param parentId 上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);


    /**
     * 根据实体条件查询
     *
     * @return
     */
    List<UserWindowDto> queryPageByDto(UserWindowDto userWindowDto);
}
