package com.test.product.trading.merchant.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.product.trading.common.enums.CodeEnum;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.common.rsp.JsonResponseFactory;
import com.test.product.trading.common.tool.DateUtil;
import com.test.product.trading.merchant.entity.MerchantAccount;
import com.test.product.trading.merchant.mapper.MerchantAccountFlowMapper;
import com.test.product.trading.merchant.mapper.MerchantAccountMapper;
import com.test.product.trading.merchant.service.MerchantAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 商家定时任务
 * </p>
 *
 * @author yh
 * @since 2025-06-17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantScheduledJob {

    private final MerchantAccountMapper merchantAccountMapper;

    private final MerchantAccountFlowMapper merchantAccountFlowMapper;

    private final MerchantAccountService merchantAccountService;

    /**
     * 每日定时结算
     *
     * @return JsonResponse
     */
    @Async
    @Scheduled(cron = "${merchant.scheduled.clear.time.scheduled}")
    public JsonResponse clearBatch() {
        try {
            // 前天商家的账户余额
            String beforeYesterday = DateUtil.getDateAddYMD(new Date(), -2);
            List<MerchantAccount> beforeYesterdayMAList = merchantAccountMapper.selectList(
                    new QueryWrapper<MerchantAccount>().lambda()
                            .eq(MerchantAccount::getSetDt, beforeYesterday));
            Map<Long, Double> beforeYesterdayBalanceMap = getBalanceMap(beforeYesterdayMAList);
            // 昨天商家的账户id
            String yesterday = DateUtil.getDateAddYMD(new Date(), -1);
            List<MerchantAccount> yesterdayMAList = merchantAccountMapper.selectList(
                    new QueryWrapper<MerchantAccount>().lambda()
                            .eq(MerchantAccount::getSetDt, yesterday));
            Map<Long, Long> yesterdayMAIdMap = getMAIdMap(yesterdayMAList);

            // 输出昨日商家余额
            List<MerchantAccount> outList = new ArrayList<>();
            // 昨日商家库存中卖出的商品订单价值，与商家账户余额流水对账
            List<MerchantAccount> yesMerchantAccount = merchantAccountFlowMapper
                    .sumMerchantAccountFlowByCreateDt(yesterday);
            for (MerchantAccount ma : yesMerchantAccount) {
                Long merchantId = ma.getMerchantId();
                double balance = ma.getBalance().doubleValue();
                String setDt = ma.getSetDt();
                String currency = ma.getCurrency();
                // 对账没问题的，更新昨日商家账户余额
                if (currency.equals(String.valueOf(CodeEnum.StatusEnum.SUCCESS.getCode()))) {
                    double beforeYesterdayBalance = beforeYesterdayBalanceMap.get(merchantId);
                    if (ObjectUtils.isEmpty(beforeYesterdayBalance)) beforeYesterdayBalance = 0;
                    BigDecimal newBalance = BigDecimal.valueOf(beforeYesterdayBalance + balance);
                    MerchantAccount maEty = MerchantAccount.builder().merchantId(merchantId)
                            .balance(newBalance).currency(CodeEnum.CurrencyEnum.CNY.getCode())
                            .setDt(yesterday).build();
                    if (yesterdayMAIdMap.containsKey(merchantId)) {
                        if (ObjectUtils.isNotEmpty(yesterdayMAIdMap.get(merchantId))) {
                            maEty.setId(yesterdayMAIdMap.get(merchantId));
                        }
                    }
                    outList.add(maEty);
                } else {
                    log.error("MerchantScheduledJob clearBatch对账不通过！merchantId={} setDt={} amount={}", merchantId, setDt, balance);
                }
            }

            // 更新商家账户的昨日余额
            boolean r = merchantAccountService.saveOrUpdateBatch(outList);
            if (r) {
                log.info("MerchantScheduledJob clearBatch 每日定时结算完成！yesterday={} outList={}", yesterday, outList);
            } else {
                log.error("MerchantScheduledJob clearBatch 每日定时结算失败！yesterday={} outList={}", yesterday, outList);
            }
            return JsonResponseFactory.success(outList);
        } catch (Exception e) {
            log.error("MerchantScheduledJob clearBatch每日定时结算异常！ {}", e.getMessage());
            e.printStackTrace();
            return JsonResponseFactory.error(99999);
        }

    }

    public Map<Long, Double> getBalanceMap(List<MerchantAccount> merchantAccountList) {
        Map<Long, Double> map = new HashMap<>();
        for (MerchantAccount ma : merchantAccountList) {
            map.put(ma.getMerchantId(), ma.getBalance().doubleValue());
        }

        return map;
    }

    public Map<Long, Long> getMAIdMap(List<MerchantAccount> merchantAccountList) {
        Map<Long, Long> map = new HashMap<>();
        for (MerchantAccount ma : merchantAccountList) {
            map.put(ma.getMerchantId(), ma.getId());
        }

        return map;
    }

}
