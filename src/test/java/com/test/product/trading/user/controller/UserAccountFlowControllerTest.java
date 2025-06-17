package com.test.product.trading.user.controller;

import com.alibaba.fastjson.JSON;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.user.entity.RechargeVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserAccountFlowControllerTest {

    @Resource
    private UserAccountFlowController userAccountFlowController;

    /**
     * 用户账户充值
     *
     * @return JsonResponse
     */
    @Test
    public void rechargeUserAccount() throws Exception {
        RechargeVO rechargeVO = new RechargeVO();
        rechargeVO.setUserNumber("13920002002");
        rechargeVO.setAmount(BigDecimal.valueOf(1000));
        JsonResponse rsp = userAccountFlowController.rechargeUserAccount(rechargeVO) ;
        log.info(JSON.toJSONString(rsp));
        Assert.assertTrue(200 == rsp.getStatus());
    }

}