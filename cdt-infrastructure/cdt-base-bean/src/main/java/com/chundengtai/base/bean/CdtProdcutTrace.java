package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Royal
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CdtProdcutTrace implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * id
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /**
   * 产品名称
   */
  private String name;

  /**
   * 产品主图
   */
  private String mainImage;

  /**
   * 产品附图
   */
  private String figureImage;

  /**
   * 产品二维码
   */
  private String qrcode;

  /**
   * 产品对应小程序二维码图片
   */
  private String mpQrcode;

  /**
   * 商品产地
   */
  private String originGoods;

  /**
   * 净含量
   */
  private String height;

  /**
   * 生产日期
   */
  private String produceDateCode;

  /**
   * 保质期
   */
  private String expirationDate;

  /**
   * 存储条件
   */
  private String storageCondition;

  /**
   * 平台批次号
   */
  private String platformBatchNumber;

  /**
   * 产品简介
   */
  private String productBrief;

  /**
   * 产品访问次数
   */
  private Integer accessCount;

  /**
   * 最后访问时间
   */
  private Date lastAccessTime;

  /**
   * 生产企业_批次号
   */
  private String productionEnterpriseNo;

  /**
   * 委托方名称
   */
  private String responsibilityName;

  /**
   * 委托方地址
   */
  private String responsibilityAddress;

  /**
   * 生产商名称
   */
  private String manufacturerName;

  /**
   * 生产商地址
   */
  private String manufacturerAddress;

  /**
   * 经销商名称
   */
  private String distributorName;

  /**
   * 经销商地址
   */
  private String distributorAddress;

  /**
   * 经销商联系方式
   */
  private String distributorContact;

  /**
   * 原料信息
   */
  private String materialInfo;

  /**
   * 原产国
   */
  private String materialCountryOrigin;

  /**
   * 乐观锁
   */
  private Integer revision;

  /**
   * 创建人
   */
  private String createdBy;

  /**
   * 创建时间
   */
  private Date createdTime;

  /**
   * 更新人
   */
  private String updatedBy;

  /**
   * 更新时间
   */
  private Date updatedTime;

  /**
   * 产品详情
   */
  private String productDesc;


}
