package com.test.product.trading.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.product.trading.common.constant.ConstDefine;
import com.test.product.trading.common.enums.CodeEnum;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.common.rsp.JsonResponseFactory;
import com.test.product.trading.common.tool.DateUtil;
import com.test.product.trading.merchant.entity.MerchantAccount;
import com.test.product.trading.merchant.entity.MerchantInfo;
import com.test.product.trading.merchant.mapper.MerchantAccountMapper;
import com.test.product.trading.merchant.mapper.MerchantInfoMapper;
import com.test.product.trading.merchant.service.MerchantAccountService;
import com.test.product.trading.merchant.service.MerchantInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商家信息表 服务实现类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantInfoServiceImpl extends ServiceImpl<MerchantInfoMapper, MerchantInfo> implements MerchantInfoService {

    private final MerchantInfoMapper merchantInfoMapper;

    private final MerchantAccountMapper merchantAccountMapper;

    private final MerchantAccountService merchantAccountService;

    /**
     * 添加商户
     *
     * @param merchantInfo 参数
     * @return JsonResponse
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse addMerchant(MerchantInfo merchantInfo) {
        log.info("MerchantInfoServiceImpl addMerchant merchantInfo:{} ", JSON.toJSONString(merchantInfo));
        String merchantNumber = merchantInfo.getMerchantNumber();
        if (StringUtils.isEmpty(merchantNumber)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "merchantNumber");
        String merchantName = merchantInfo.getMerchantName();
        if (StringUtils.isEmpty(merchantName)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "merchantName");

        try {
            Long id = merchantInfo.getId();
            if (ObjectUtils.isNotEmpty(id)) {
                MerchantInfo existEntity = merchantInfoMapper.selectOne(
                        new QueryWrapper<MerchantInfo>().lambda()
                                .eq(MerchantInfo::getId, id)
                                .last(ConstDefine.LIMIT_ONE));
                if (ObjectUtils.isEmpty(existEntity)) {
                    return JsonResponseFactory.build(10000, "入参错误，无法执行", "id不存在无法更新");
                }
            }
            // 添加商户信息
            Long merchantInfoId = addMerchantInfo(merchantInfo);
            if (ObjectUtils.isEmpty(merchantInfoId)) throw new RuntimeException("添加商户信息异常");
            // 初始化商家账户余额
            if (ObjectUtils.isEmpty(id)) {
                MerchantAccount existEnt = merchantAccountMapper.selectOne(
                        new QueryWrapper<MerchantAccount>().lambda()
                                .eq(MerchantAccount::getMerchantId, merchantInfoId)
                                .last(ConstDefine.LIMIT_ONE));
                if (ObjectUtils.isEmpty(existEnt)) {
                    String yesterday = DateUtil.getDateAddYMD(new Date(), -1);
                    MerchantAccount merchantAccountYesterday = MerchantAccount.builder().merchantId(merchantInfoId)
                            .balance(BigDecimal.valueOf(0)).currency(CodeEnum.CurrencyEnum.CNY.getCode())
                            .setDt(yesterday).build();
                    JsonResponse yesRsp = merchantAccountService.addMerchantAccount(merchantAccountYesterday);
                    if (200 != yesRsp.getStatus()) throw new RuntimeException("初始化商家账户昨日余额异常");
                    MerchantAccount merchantAccount = MerchantAccount.builder().merchantId(merchantInfoId)
                            .balance(BigDecimal.valueOf(0)).currency(CodeEnum.CurrencyEnum.CNY.getCode())
                            .setDt(CodeEnum.DateEnum.LATEST.getCode()).build();
                    JsonResponse latestRsp = merchantAccountService.addMerchantAccount(merchantAccount);
                    if (200 != latestRsp.getStatus()) throw new RuntimeException("初始化商家账户最新余额异常");
                    return latestRsp;
                }
            }
        } catch (Exception e) {
            log.error("MerchantInfoServiceImpl addMerchant异常！{}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("处理异常", e);
        }

        return JsonResponseFactory.success(merchantInfo);
    }

    /**
     * 添加商户信息
     *
     * @param merchantInfo 参数
     * @return JsonResponse
     */
    public Long addMerchantInfo(MerchantInfo merchantInfo) throws Exception {
        Long id = merchantInfo.getId();
        if (ObjectUtils.isEmpty(id)) {
            MerchantInfo existEntity = merchantInfoMapper.selectOne(
                    new QueryWrapper<MerchantInfo>().lambda()
                            .eq(MerchantInfo::getMerchantNumber, merchantInfo.getMerchantNumber())
                            .last(ConstDefine.LIMIT_ONE));
            // 手机号存在则更新
            if (ObjectUtils.isNotEmpty(existEntity)) {
                merchantInfo.setId(existEntity.getId());
            }
        }

        boolean r = saveOrUpdate(merchantInfo);
        return merchantInfo.getId();
    }

}
