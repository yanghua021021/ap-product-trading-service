package com.test.product.trading.merchant.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 添加商品数量入参实体
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStockVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商家号码
     */
    private String merchantNumber;

    /**
     * 商品sku
     */
    private String sku;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 添加商品数量
     */
    private Integer productQty;
}
