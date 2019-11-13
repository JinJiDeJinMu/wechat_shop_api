package com.chundengtai.base.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:41
 */
@TableName("nideshop_attribute")
public class AttributeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //
    private Integer attribute_category_id;
    //
    private String name;
    //
    private Integer input_type;
    //
    private String value;
    //
    private Integer sort_order;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAttribute_category_id() {
        return attribute_category_id;
    }

    public void setAttribute_category_id(Integer attribute_category_id) {
        this.attribute_category_id = attribute_category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInput_type() {
        return input_type;
    }

    public void setInput_type(Integer input_type) {
        this.input_type = input_type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSort_order() {
        return sort_order;
    }

    public void setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
    }
}
