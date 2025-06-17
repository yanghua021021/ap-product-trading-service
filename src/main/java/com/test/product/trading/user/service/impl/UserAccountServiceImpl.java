package com.test.product.trading.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.product.trading.common.constant.ConstDefine;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.common.rsp.JsonResponseFactory;
import com.test.product.trading.merchant.entity.MerchantAccount;
import com.test.product.trading.merchant.mapper.MerchantAccountMapper;
import com.test.product.trading.user.entity.UserAccount;
import com.test.product.trading.user.entity.UserInfo;
import com.test.product.trading.user.mapper.UserAccountMapper;
import com.test.product.trading.user.mapper.UserInfoMapper;
import com.test.product.trading.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账户余额表 服务实现类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    private final UserAccountMapper userAccountMapper;

    private final UserInfoMapper userInfoMapper;

    /**
     * 添加或更新用户账户余额
     *
     * @param userAccount 参数
     * @return JsonResponse
     */
    @Override
    public JsonResponse addUserAccount(UserAccount userAccount) {
        log.info("UserAccountServiceImpl addUserAccount userAccount:{} ", JSON.toJSONString(userAccount));
        Long userId = userAccount.getUserId();
        if (ObjectUtils.isEmpty(userId)) {
            return JsonResponseFactory.build(10000, "入参错误，无法执行", "userId");
        }
        String setDt = userAccount.getSetDt();
        if (StringUtils.isEmpty(setDt)) {
            return JsonResponseFactory.build(10000, "入参错误，无法执行", "setDt");
        }
        try {
            UserAccount existEntity = userAccountMapper.selectOne(
                    new QueryWrapper<UserAccount>().lambda()
                            .eq(UserAccount::getUserId, userId)
                            .eq(UserAccount::getSetDt, setDt)
                            .last(ConstDefine.LIMIT_ONE));
            if (ObjectUtils.isNotEmpty(existEntity)) {
                userAccount.setId(existEntity.getId());
            }
            boolean r = saveOrUpdate(userAccount);
        } catch (Exception e) {
            log.error("UserAccountServiceImpl addUserAccount异常！{}", e.getMessage());
            e.printStackTrace();
            return JsonResponseFactory.error(99999);
        }

        return JsonResponseFactory.success(userAccount);
    }

    /**
     * 获取用户账户
     *
     * @param userNumber 参数
     * @param setDt 参数
     * @return UserAccount
     */
    @Override
    public UserAccount getUserAccount(String userNumber, String setDt) {
        try {
            UserInfo userInfo = userInfoMapper.selectOne(
                    new QueryWrapper<UserInfo>().lambda()
                            .eq(UserInfo::getUserNumber, userNumber)
                            .last(ConstDefine.LIMIT_ONE));
            if (ObjectUtils.isNotEmpty(userInfo)) {
                Long userId = userInfo.getId();
                UserAccount userAccount = userAccountMapper.selectOne(
                        new QueryWrapper<UserAccount>().lambda()
                                .eq(UserAccount::getUserId, userId)
                                .eq(UserAccount::getSetDt, setDt)
                                .last(ConstDefine.LIMIT_ONE));
                return userAccount;
            }
        } catch (Exception e) {
            log.error("UserAccountServiceImpl getUserAccount异常！{} {} {}", userNumber, setDt, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
