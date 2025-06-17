package com.test.product.trading.order.controller;


import com.alibaba.fastjson.JSON;
import com.test.product.trading.common.config.UrlConfig;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.order.entity.BuyProductOrderVO;
import com.test.product.trading.order.service.OrderInfoService;
import com.test.product.trading.user.entity.UserInfo;
import com.test.product.trading.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单信息表 前端控制器
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderInfoController {

    private final OrderInfoService orderInfoService;

    /**
     * 用户购买商品订单
     *
     * @param buyProductOrderVO 参数
     * @return JsonResponse
     */
    @PostMapping(value = UrlConfig.URL_ORDER_PRODUCT_BUY_V1, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse buyProductOrder(@RequestBody BuyProductOrderVO buyProductOrderVO) {
        log.info("OrderInfoController buyProductOrder buyProductOrderVO:{} ", JSON.toJSONString(buyProductOrderVO));
        return orderInfoService.buyProductOrder(buyProductOrderVO);
    }

}

