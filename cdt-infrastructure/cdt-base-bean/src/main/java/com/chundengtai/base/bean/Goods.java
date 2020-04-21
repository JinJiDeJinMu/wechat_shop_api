package com.chundengtai.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Royal
 * @since 2019-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("nideshop_goods")
@Document(indexName = "cdt", type = "goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer categoryId;

    private String goodsSn;

    /**
     * 浏览量
     */
    private Integer browse;

    private String name;

    private Integer brandId;

    /**
     * 商品库存
     */
    private Integer goodsNumber;

    private String keywords;

    private String goodsBrief;

    private String goodsDesc;

    private Boolean isOnSale;

    private Date addTime;

    private Integer sortOrder;

    private Boolean isDelete;

    private Integer attributeCategory;

    /**
     * 最低成功价
     */
    private BigDecimal counterPrice;

    /**
     * 附加价格
     */
    private BigDecimal extraPrice;

    private Boolean isNew;

    /**
     * 商品单位
     */
    private String goodsUnit;

    /**
     * 商品主图
     */
    private String primaryPicUrl;

    /**
     * 商品列表图
     */
    private String listPicUrl;

    /**
     * 零售价格
     */
    private BigDecimal retailPrice;

    /**
     * 销售量
     */
    private Integer sellVolume;

    /**
     * 主sku　product_id
     */
    private Integer primaryProductId;

    /**
     * 单位价格，单价
     */
    private BigDecimal unitPrice;

    private String promotionDesc;

    private String promotionTag;

    /**
     * APP专享价
     */
    private BigDecimal appExclusivePrice;

    /**
     * 是否是APP专属
     */
    private Boolean isAppExclusive;

    private Boolean isLimited;

    private Boolean isHot;

    private BigDecimal marketPrice;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 修改人ID
     */
    private Long updateUserId;

    /**
     * 修改时间
     */
    private Date updateTime;

    private Long createUserDeptId;

    /**
     * 商品类型,1:普通商品,2:秒杀,3:团购,4:砍价,5快递代取,6核销
     */
    @TableField("is_secKill")
    private Integer isSeckill;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 是否是服务型商品
     */
    private Integer isService;

    /**
     * 分佣金百分比
     */
    private Double brokeragePercent;

    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 成团时间(分钟)
     */
    private Integer successTime;

    /**
     * 成团人数
     */
    private Integer successPeople;

    /**
     * 团购价格(元)
     */
    private BigDecimal groupPrice;

    private Integer purchaseType;

    private Integer goodsType;

    private String schoolName;


}
