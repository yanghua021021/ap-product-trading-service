package com.test.product.trading.merchant.controller;


import com.alibaba.fastjson.JSON;
import com.test.product.trading.common.config.UrlConfig;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.merchant.entity.ProductStockVO;
import com.test.product.trading.merchant.service.ProductStockService;
import com.test.product.trading.user.entity.RechargeVO;
import com.test.product.trading.user.service.UserAccountFlowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品库存表 前端控制器
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductStockController {

    private final ProductStockService productStockService;

    /**
     * 添加商品数量
     *
     * @param productStockVO 参数
     * @return JsonResponse
     */
    @PostMapping(value = UrlConfig.URL_PRODUCT_STOCK_ADD_V1, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse addMerchantProductQty(@RequestBody ProductStockVO productStockVO) {
        log.info("ProductStockController addMerchantProductQty productStockVO:{} ", JSON.toJSONString(productStockVO));
        return productStockService.addMerchantProductQty(productStockVO);
    }

}

