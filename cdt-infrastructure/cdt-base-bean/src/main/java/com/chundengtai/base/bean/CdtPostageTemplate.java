package com.chundengtai.base.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Royal
 * @since 2020-02-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class CdtPostageTemplate implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * id
     */
      @TableId(value = "id", type = IdType.AUTO)
        private Integer id;

      /**
     * 模板标题
     */
      private String topic;

      /**
     * 快递公司
     */
      private String expressCompany;

      /**
     * 邮费统计类型
     */
      private Integer postageType;

      /**
     * 计费方式（0：按件，1：按重）
     */
      private Integer chargingType;

      /**
     * 配送区域
     */
      private String address;

      /**
     * 首件
     */
      private Integer first;

      /**
     * 运费
     */
      private BigDecimal money;

      /**
     * 续件
     */
      private Integer second;

      /**
     * 续费
     */
      private BigDecimal renewMoney;

      /**
     * 创建时间
     */
      private Date createTime;

      /**
     * 更新时间
     */
      private Date updateTime;

      /**
     * 备注
     */
      private String remark;

      /**
     * 创建人
     */
      private String creater;


}
