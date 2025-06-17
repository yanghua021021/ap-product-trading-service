package com.test.product.trading.user.entity;

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
 * 用户充值入参实体
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RechargeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户号码
     */
    private String userNumber;

    /**
     * 金额
     */
    private BigDecimal amount;
}
