package com.test.product.trading.merchant.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商家账户余额表
 * </p>
 *
 * @author yh
 * @since 2025-06-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("merchant_account")
public class MerchantAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家id
     */
    private Long merchantId;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 币种
     */
    private String currency;

    /**
     * 结算日期
     */
    private String setDt;

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
