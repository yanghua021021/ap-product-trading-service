package com.test.product.trading.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.order.entity.AddOrderVO;
import com.test.product.trading.order.entity.BuyProductOrderVO;
import com.test.product.trading.order.entity.OrderInfo;

/**
 * <p>
 * 订单信息表 服务类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 用户购买商品
     *
     * @param buyProductOrderVO 参数
     * @return JsonResponse
     */
    JsonResponse buyProductOrder(BuyProductOrderVO buyProductOrderVO);

    /**
     * 生成订单信息
     *
     * @param addOrderVO 参数
     * @return JsonResponse
     */
    JsonResponse addOrder(AddOrderVO addOrderVO) throws Exception;

}
