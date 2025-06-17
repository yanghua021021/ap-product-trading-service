package com.test.product.trading.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 生成订单入参实体
 * </p>
 *
 * @author yh
 * @since 2025-06-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrderVO extends OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 商品单价
     */
    private BigDecimal price;
}
