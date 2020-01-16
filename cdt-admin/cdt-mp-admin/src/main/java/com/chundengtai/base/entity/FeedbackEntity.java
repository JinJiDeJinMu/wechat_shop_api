package com.chundengtai.base.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Data
public class FeedbackEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer msgId;
    //父节点
//    private Integer parentId;
    //会员Id
    private Integer userId;
    //会员名称
    private String userName;

    //移动电话
    private String mobile;
    //邮件
//    private String userEmail;
    //标题
//    private String msgTitle;
    //类型
    private Integer feedType;
    //状态
    private Integer status;
    //详细内容
    private String content;
    //反馈时间
    private Date addTime;
    //图片
//    private String messageImg;
    //订单Id
//    private Integer orderId;
    //
//    private Integer msgArea;
    //商户ID
    private Long merchant_id;

}
