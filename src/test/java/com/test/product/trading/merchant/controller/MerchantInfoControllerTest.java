package com.test.product.trading.merchant.controller;

import com.alibaba.fastjson.JSON;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.merchant.entity.MerchantInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MerchantInfoControllerTest {

    @Resource
    private MerchantInfoController merchantInfoController;

    /**
     * 添加商户
     *
     * @return JsonResponse
     */
    @Test
    public void addMerchant() throws Exception {
        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setMerchantNumber("13910001001");
        merchantInfo.setMerchantName("小张的店铺");
        JsonResponse rsp = merchantInfoController.addMerchant(merchantInfo);
        log.info(JSON.toJSONString(rsp));
        Assert.assertTrue(200 == rsp.getStatus());
    }

}