package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Royal
 * @since 2019-12-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("nideshop_comment_picture")
public class CommentPicture implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 评价Id
     */
    private Long commentId;

    /**
     * 评价图片
     */
    private String picUrl;

    /**
     * 排序
     */
    private Integer sortOrder;

    private Integer status;


}
