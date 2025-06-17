package com.test.product.trading.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.product.trading.common.config.RedisKeysConfig;
import com.test.product.trading.common.constant.ConstDefine;
import com.test.product.trading.common.enums.CodeEnum;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.common.rsp.JsonResponseFactory;
import com.test.product.trading.common.tool.DateUtil;
import com.test.product.trading.common.tool.RedisUtil;
import com.test.product.trading.merchant.entity.MerchantInfo;
import com.test.product.trading.merchant.entity.ProductStock;
import com.test.product.trading.user.entity.RechargeVO;
import com.test.product.trading.user.entity.UserAccount;
import com.test.product.trading.user.entity.UserAccountFlow;
import com.test.product.trading.user.entity.UserInfo;
import com.test.product.trading.user.mapper.UserAccountFlowMapper;
import com.test.product.trading.user.mapper.UserAccountMapper;
import com.test.product.trading.user.mapper.UserInfoMapper;
import com.test.product.trading.user.service.UserAccountFlowService;
import com.test.product.trading.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 用户账户流水表 服务实现类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserAccountFlowServiceImpl extends ServiceImpl<UserAccountFlowMapper, UserAccountFlow> implements UserAccountFlowService {

    private final UserInfoMapper userInfoMapper;

    private final UserAccountMapper userAccountMapper;

    private final UserAccountService userAccountService;

    private final StringRedisTemplate redisTemplate;

//    @Qualifier("taskExecutor")
//    @Autowired
//    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 添加用户账户流水
     *
     * @param userAccountFlow 参数
     * @return JsonResponse
     */
    @Override
    public JsonResponse addUserAccountFlow(UserAccountFlow userAccountFlow) {
        log.info("UserAccountFlowServiceImpl addUserAccountFlow userAccountFlow:{} ", JSON.toJSONString(userAccountFlow));
        Long accountId = userAccountFlow.getAccountId();
        if (ObjectUtils.isEmpty(accountId)) {
            return JsonResponseFactory.build(10000, "入参错误，无法执行", "accountId不存在");
        }
        BigDecimal amount = userAccountFlow.getAmount();
        if (ObjectUtils.isEmpty(amount)) {
            return JsonResponseFactory.build(10000, "入参错误，无法执行", "amount不存在");
        }
        try {
//            CompletableFuture.runAsync(() -> {
//                saveOrUpdate(userAccountFlow);
//            }, taskExecutor);
            boolean r = saveOrUpdate(userAccountFlow);
        } catch (Exception e) {
            log.error("UserAccountFlowServiceImpl addUserAccountFlow异常！{}", e.getMessage());
            e.printStackTrace();
            return JsonResponseFactory.error(99999);
        }

        return JsonResponseFactory.success(userAccountFlow);
    }

    /**
     * 用户账户充值
     *
     * @param rechargeVO 参数
     * @return JsonResponse
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse rechargeUserAccount(RechargeVO rechargeVO) {
        log.info("UserAccountFlowServiceImpl rechargeUserAccount rechargeVO:{} ", JSON.toJSONString(rechargeVO));
        String userNumber = rechargeVO.getUserNumber();
        if (StringUtils.isEmpty(userNumber)) {
            return JsonResponseFactory.build(10000, "入参错误，无法执行", "userNumber不存在");
        }
        BigDecimal amount = rechargeVO.getAmount();
        if (ObjectUtils.isEmpty(amount)) {
            return JsonResponseFactory.build(10000, "入参错误，无法执行", "amount不存在");
        }

        String lockKey = RedisKeysConfig.LOCK_USER_ACCOUNT_FLOW_RECHARGE_KEY + ":" + userNumber;
        String lockValue = lockKey + ":" + String.valueOf(amount);
        try {
            // 如果未获取到分布式锁，不执行后续代码
            boolean tryLock = RedisUtil.tryLock(redisTemplate, lockKey, lockValue, 60);
            if (!tryLock) {
                log.info("UserAccountFlowServiceImpl rechargeUserAccount，未获取到分布式锁，不执行后续代码 {} {}", lockKey, lockValue);
                return JsonResponseFactory.build(90001, "未获取到分布式锁", lockKey);
            }

            // 检查用户
            UserInfo userInfo = userInfoMapper.selectOne(
                    new QueryWrapper<UserInfo>().lambda()
                            .eq(UserInfo::getUserNumber, userNumber)
                            .last(ConstDefine.LIMIT_ONE));
            if (ObjectUtils.isEmpty(userInfo)) {
                log.info("UserAccountFlowServiceImpl rechargeUserAccount，用户不存在 userNumber={}", userNumber);
                return JsonResponseFactory.build(10000, "入参错误，无法执行", "用户不存在"+userNumber);
            }
            // 检查用户账户
            Long userId = userInfo.getId();
            UserAccount userAccount = userAccountMapper.selectOne(
                    new QueryWrapper<UserAccount>().lambda()
                            .eq(UserAccount::getUserId, userId)
                            .eq(UserAccount::getSetDt, CodeEnum.DateEnum.LATEST.getCode())
                            .last(ConstDefine.LIMIT_ONE));
            if (ObjectUtils.isEmpty(userAccount)) {
                log.info("UserAccountFlowServiceImpl rechargeUserAccount，用户账户不存在 userId={}", userId);
                return JsonResponseFactory.build(10000, "入参错误，无法执行", "用户账户不存在"+userId);
            }

            // 更新账户余额
            BigDecimal currentBalance = userAccount.getBalance();
            BigDecimal latestBalance = currentBalance.add(amount);
            userAccount.setBalance(latestBalance);
            userAccount.setUpdateTime(new Date());
            JsonResponse userAccountResponse = userAccountService.addUserAccount(userAccount);
            int uaStatus = userAccountResponse.getStatus();
            log.info("UserAccountFlowServiceImpl rechargeUserAccount userAccountResponse={}", userAccountResponse);
            // 添加账户流水
            if (200 == uaStatus) {
                String today = DateUtil.tranDateMonth2String(new Date());
                UserAccountFlow userAccountFlow = UserAccountFlow.builder().accountId(userAccount.getId())
                        .amount(amount).trdType(CodeEnum.TradeTypeEnum.RECHARGE.getCode())
                        .orderId(null).createDt(today).build();
                JsonResponse userAccountFlowResponse = addUserAccountFlow(userAccountFlow);
                // 释放分布式锁
                boolean unLock = RedisUtil.unLock(redisTemplate, lockKey);
                if (200 != userAccountFlowResponse.getStatus()) throw new RuntimeException("添加账户流水异常");
                return userAccountFlowResponse;
            } else {
                throw new RuntimeException("更新账户余额异常");
            }
        } catch (Exception e) {
            log.error("UserAccountFlowServiceImpl rechargeUserAccount异常！{}", e.getMessage());
            e.printStackTrace();
            RedisUtil.unLock(redisTemplate, lockKey);
            throw new RuntimeException("处理异常", e);
        }

    }

}
