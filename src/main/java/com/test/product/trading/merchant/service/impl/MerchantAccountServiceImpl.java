package com.test.product.trading.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.product.trading.common.constant.ConstDefine;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.common.rsp.JsonResponseFactory;
import com.test.product.trading.merchant.entity.MerchantAccount;
import com.test.product.trading.merchant.entity.MerchantInfo;
import com.test.product.trading.merchant.mapper.MerchantAccountMapper;
import com.test.product.trading.merchant.mapper.MerchantInfoMapper;
import com.test.product.trading.merchant.service.MerchantAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商家账户余额表 服务实现类
 * </p>
 *
 * @author yh
 * @since 2025-06-14
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantAccountServiceImpl extends ServiceImpl<MerchantAccountMapper, MerchantAccount> implements MerchantAccountService {

    private final MerchantAccountMapper merchantAccountMapper;

    private final MerchantInfoMapper merchantInfoMapper;

    /**
     * 添加或更新商家账户余额
     *
     * @param merchantAccount 参数
     * @return JsonResponse
     */
    @Override
    public JsonResponse addMerchantAccount(MerchantAccount merchantAccount) {
        log.info("MerchantAccountServiceImpl addMerchantAccount merchantAccount:{} ", JSON.toJSONString(merchantAccount));
        Long merchantId = merchantAccount.getMerchantId();
        if (ObjectUtils.isEmpty(merchantId)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "merchantId");
        String setDt = merchantAccount.getSetDt();
        if (StringUtils.isEmpty(setDt)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "setDt");

        try {
            MerchantAccount existEntity = merchantAccountMapper.selectOne(
                    new QueryWrapper<MerchantAccount>().lambda()
                            .eq(MerchantAccount::getMerchantId, merchantId)
                            .eq(MerchantAccount::getSetDt, setDt)
                            .last(ConstDefine.LIMIT_ONE));
            if (ObjectUtils.isNotEmpty(existEntity)) {
                merchantAccount.setId(existEntity.getId());
            }
            saveOrUpdate(merchantAccount);
        } catch (Exception e) {
            log.error("MerchantAccountServiceImpl addMerchantAccount异常！{}", e.getMessage());
            e.printStackTrace();
            return JsonResponseFactory.error(99999);
        }

        return JsonResponseFactory.success(merchantAccount);
    }

    /**
     * 获取商户余额
     *
     * @param merchantNumber 参数
     * @param setDt 参数
     * @return MerchantAccount
     */
    @Override
    public MerchantAccount getMerchantAccount(String merchantNumber, String setDt) {
        try {
            MerchantInfo merchantInfo = merchantInfoMapper.selectOne(
                    new QueryWrapper<MerchantInfo>().lambda()
                            .eq(MerchantInfo::getMerchantNumber, merchantNumber)
                            .last(ConstDefine.LIMIT_ONE));
            if (ObjectUtils.isNotEmpty(merchantInfo)) {
                Long merchantId = merchantInfo.getId();
                MerchantAccount merchantAccount = merchantAccountMapper.selectOne(
                        new QueryWrapper<MerchantAccount>().lambda()
                                .eq(MerchantAccount::getMerchantId, merchantId)
                                .eq(MerchantAccount::getSetDt, setDt)
                                .last(ConstDefine.LIMIT_ONE));
                return merchantAccount;
            }
        } catch (Exception e) {
            log.error("MerchantAccountServiceImpl getMerchantAccount异常！{} {} {}", merchantNumber, setDt, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
