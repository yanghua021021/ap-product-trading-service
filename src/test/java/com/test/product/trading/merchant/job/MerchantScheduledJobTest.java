package com.test.product.trading.merchant.job;

import com.alibaba.fastjson.JSON;
import com.test.product.trading.common.rsp.JsonResponse;
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
public class MerchantScheduledJobTest {

    @Resource
    private MerchantScheduledJob merchantScheduledJob;

    /**
     * 每日定时结算
     *
     * @return
     */
    @Test
    public void clearBatch() throws Exception {
        JsonResponse rsp = merchantScheduledJob.clearBatch();
        log.info(JSON.toJSONString(rsp));
        Assert.assertTrue(200 == rsp.getStatus());
    }

}