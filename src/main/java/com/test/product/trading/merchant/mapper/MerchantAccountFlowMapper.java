package com.test.product.trading.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.product.trading.merchant.entity.MerchantAccount;
import com.test.product.trading.merchant.entity.MerchantAccountFlow;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 * 商家账户流水表 Mapper 接口
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
public interface MerchantAccountFlowMapper extends BaseMapper<MerchantAccountFlow> {

    /**
     * 按日汇总商家账户流水
     *
     * @param createDt 参数
     * @return List<MerchantAccountFlow>
     */
    List<MerchantAccount> sumMerchantAccountFlowByCreateDt(@Param("createDt") String createDt);

}
