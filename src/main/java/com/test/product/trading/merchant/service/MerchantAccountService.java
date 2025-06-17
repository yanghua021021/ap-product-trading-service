package com.test.product.trading.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.merchant.entity.MerchantAccount;
import com.test.product.trading.merchant.entity.MerchantInfo;
import com.test.product.trading.user.entity.UserAccount;

/**
 * <p>
 * 商家账户余额表 服务类
 * </p>
 *
 * @author yh
 * @since 2025-06-14
 */
public interface MerchantAccountService extends IService<MerchantAccount> {

    /**
     * 添加或更新商家账户余额
     *
     * @param merchantAccount 参数
     * @return JsonResponse
     */
    JsonResponse addMerchantAccount(MerchantAccount merchantAccount);

    /**
     * 获取商户余额
     *
     * @param merchantNumber 参数
     * @param setDt 参数
     * @return MerchantAccount
     */
    MerchantAccount getMerchantAccount(String merchantNumber, String setDt);
}
