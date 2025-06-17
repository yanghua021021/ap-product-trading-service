package com.test.product.trading.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.product.trading.common.constant.ConstDefine;
import com.test.product.trading.common.enums.CodeEnum;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.common.rsp.JsonResponseFactory;
import com.test.product.trading.user.entity.UserAccount;
import com.test.product.trading.user.entity.UserInfo;
import com.test.product.trading.user.mapper.UserAccountMapper;
import com.test.product.trading.user.mapper.UserInfoMapper;
import com.test.product.trading.user.service.UserAccountService;
import com.test.product.trading.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    private final UserAccountMapper userAccountMapper;

    private final UserAccountService userAccountService;

    /**
     * 添加用户
     *
     * @param userInfo 参数
     * @return JsonResponse
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse addUser(UserInfo userInfo) {
        log.info("UserInfoServiceImpl addUser userInfo:{} ", JSON.toJSONString(userInfo));
        String userNumber = userInfo.getUserNumber();
        if (StringUtils.isEmpty(userNumber)) {
            return JsonResponseFactory.build(10000, "入参错误，无法执行", "userNumber");
        }
        String userName = userInfo.getUserName();
        if (StringUtils.isEmpty(userName)) {
            return JsonResponseFactory.build(10000, "入参错误，无法执行", "userName");
        }

        try {
            Long id = userInfo.getId();
            if (ObjectUtils.isNotEmpty(id)) {
                UserInfo existEntity = userInfoMapper.selectOne(
                        new QueryWrapper<UserInfo>().lambda()
                                .eq(UserInfo::getId, id)
                                .last(ConstDefine.LIMIT_ONE));
                if (ObjectUtils.isEmpty(existEntity)) {
                    return JsonResponseFactory.build(10000, "入参错误，无法执行", "id不存在无法更新");
                }
            }
            // 添加用户信息
            Long userInfoId = addUserInfo(userInfo);
            // 初始化用户账户余额
            if (ObjectUtils.isEmpty(id)) {
                UserAccount existEnt = userAccountMapper.selectOne(
                        new QueryWrapper<UserAccount>().lambda()
                                .eq(UserAccount::getUserId, userInfoId)
                                .last(ConstDefine.LIMIT_ONE));
                if (ObjectUtils.isEmpty(existEnt)) {
                    UserAccount userAccount = new UserAccount();
                    userAccount.setUserId(userInfoId);
                    userAccount.setBalance(BigDecimal.valueOf(0));
                    userAccount.setCurrency(CodeEnum.CurrencyEnum.CNY.getCode());
                    userAccount.setSetDt(CodeEnum.DateEnum.LATEST.getCode());
                    return userAccountService.addUserAccount(userAccount);
                }
            }
        } catch (Exception e) {
            log.error("UserInfoServiceImpl addUser异常！{}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("处理异常", e);
        }

        return JsonResponseFactory.success(userInfo);
    }

    /**
     * 添加用户信息
     *
     * @param userInfo 参数
     * @return JsonResponse
     */
    public Long addUserInfo(UserInfo userInfo) throws Exception {
        Long id = userInfo.getId();
        if (ObjectUtils.isEmpty(id)) {
            UserInfo existEntity = userInfoMapper.selectOne(
                    new QueryWrapper<UserInfo>().lambda()
                            .eq(UserInfo::getUserNumber, userInfo.getUserNumber())
                            .last(ConstDefine.LIMIT_ONE));
            // 手机号存在则更新
            if (ObjectUtils.isNotEmpty(existEntity)) {
                userInfo.setId(existEntity.getId());
            }
        }

        boolean r = saveOrUpdate(userInfo);
        return userInfo.getId();
    }

}
