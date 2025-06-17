package com.test.product.trading.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.merchant.entity.MerchantInfo;
import com.test.product.trading.user.entity.UserInfo;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 添加用户
     *
     * @param userInfo 参数
     * @return JsonResponse
     */
    JsonResponse addUser(UserInfo userInfo);

}
