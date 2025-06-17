package com.test.product.trading.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户购买商品订单入参实体
 * </p>
 *
 * @author yh
 * @since 2025-06-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyProductOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户号码
     */
    private String userNumber;

    /**
     * 商家号码
     */
    private String merchantNumber;

    /**
     * 商品sku
     */
    private String sku;

    /**
     * 商品数量
     */
    private Integer productQty;
}
