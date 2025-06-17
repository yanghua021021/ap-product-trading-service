package com.test.product.trading.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.user.entity.RechargeVO;
import com.test.product.trading.user.entity.UserAccount;
import com.test.product.trading.user.entity.UserAccountFlow;

/**
 * <p>
 * 用户账户流水表 服务类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
public interface UserAccountFlowService extends IService<UserAccountFlow> {

    /**
     * 添加用户账户流水
     *
     * @param userAccountFlow 参数
     * @return JsonResponse
     */
    JsonResponse addUserAccountFlow(UserAccountFlow userAccountFlow);

    /**
     * 用户账户充值
     *
     * @param rechargeVO 参数
     * @return JsonResponse
     */
    JsonResponse rechargeUserAccount(RechargeVO rechargeVO);

}
