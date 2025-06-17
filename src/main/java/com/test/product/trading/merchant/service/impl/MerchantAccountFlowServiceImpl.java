package com.test.product.trading.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.product.trading.merchant.entity.MerchantAccountFlow;
import com.test.product.trading.merchant.mapper.MerchantAccountFlowMapper;
import com.test.product.trading.merchant.service.MerchantAccountFlowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商家账户流水表 服务实现类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantAccountFlowServiceImpl extends ServiceImpl<MerchantAccountFlowMapper, MerchantAccountFlow> implements MerchantAccountFlowService {

}
