package com.test.product.trading.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.merchant.entity.MerchantAccount;
import com.test.product.trading.merchant.entity.ProductStock;
import com.test.product.trading.merchant.entity.ProductStockVO;
import com.test.product.trading.user.entity.UserAccountFlow;

/**
 * <p>
 * 商品库存表 服务类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
public interface ProductStockService extends IService<ProductStock> {

    /**
     * 添加或更新商品库存表
     *
     * @param productStock 参数
     * @return JsonResponse
     */
    JsonResponse addProductStock(ProductStock productStock);

    /**
     * 添加商品数量
     *
     * @param productStockVO 参数
     * @return JsonResponse
     */
    JsonResponse addMerchantProductQty(ProductStockVO productStockVO);

    /**
     * 获取商品
     *
     * @param merchantId 参数
     * @param sku 参数
     * @return ProductStock
     */
    ProductStock getProductStock(Long merchantId, String sku);

}
