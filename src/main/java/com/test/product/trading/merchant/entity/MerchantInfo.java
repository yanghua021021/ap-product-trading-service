package com.test.product.trading.merchant.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商家信息表
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("merchant_info")
public class MerchantInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家号码
     */
    private String merchantNumber;

    /**
     * 商家名称
     */
    private String merchantName;

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
