package com.test.product.trading.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.merchant.entity.MerchantInfo;

/**
 * <p>
 * 商家信息表 服务类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
public interface MerchantInfoService extends IService<MerchantInfo> {

    /**
     * 添加商户
     *
     * @param merchantInfo 参数
     * @return JsonResponse
     */
    JsonResponse addMerchant(MerchantInfo merchantInfo);

}
