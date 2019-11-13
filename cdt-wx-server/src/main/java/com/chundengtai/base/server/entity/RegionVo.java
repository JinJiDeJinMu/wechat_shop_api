package com.chundengtai.base.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.entity.SysRegionEntity;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:41
 */
@TableName("sys_region")
public class RegionVo {

    //主键
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //父节点
    private Integer parent_id;
    //区域名称
    private String name;
    //类型 0国家 1省份 2地市 3区县
    private Integer type;
    //区域代理Id
    private Integer agency_id;

    public RegionVo() {
    }

    public RegionVo(SysRegionEntity regionEntity) {
        id = regionEntity.getId();
        parent_id = regionEntity.getParentId();
        name = regionEntity.getName();
        type = regionEntity.getType();
        agency_id = regionEntity.getAgencyId();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(Integer agency_id) {
        this.agency_id = agency_id;
    }
}
