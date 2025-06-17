package com.test.product.trading.merchant.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商家账户流水表
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("merchant_account_flow")
public class MerchantAccountFlow implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家账户id
     */
    private Long accountId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 交易类型
     */
    private Integer trdType;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 交易日期
     */
    private String createDt;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}
