package com.test.product.trading.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.merchant.entity.MerchantAccount;
import com.test.product.trading.user.entity.UserAccount;

/**
 * <p>
 * 用户账户余额表 服务类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
public interface UserAccountService extends IService<UserAccount> {

    /**
     * 添加或更新用户账户余额
     *
     * @param userAccount 参数
     * @return JsonResponse
     */
    JsonResponse addUserAccount(UserAccount userAccount);

    /**
     * 获取用户账户
     *
     * @param userNumber 参数
     * @param setDt 参数
     * @return UserAccount
     */
    UserAccount getUserAccount(String userNumber, String setDt);
}
