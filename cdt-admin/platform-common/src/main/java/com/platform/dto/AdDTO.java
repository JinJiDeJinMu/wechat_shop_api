package com.platform.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:40
 */
@Data
@NoArgsConstructor
public class AdDTO implements Serializable {
    //主键
    private Integer id;
    //广告位置Id
    private Integer ad_position_id;
    //形式
    private Integer media_type;
    //类型
    private Integer type;
    //广告名称
    private String name;
    //链接
    private String link;
    //图片
    private String image_url;
    //内容
    private String content;
    //结束时间
    private Date end_time;
    //状态
    private Integer enabled;
}
