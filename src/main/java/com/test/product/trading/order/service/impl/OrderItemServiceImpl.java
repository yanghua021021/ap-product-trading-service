package com.test.product.trading.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.product.trading.order.entity.OrderItem;
import com.test.product.trading.order.mapper.OrderItemMapper;
import com.test.product.trading.order.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
