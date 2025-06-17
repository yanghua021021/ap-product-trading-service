package com.test.product.trading.order.controller;

import com.alibaba.fastjson.JSON;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.order.entity.BuyProductOrderVO;
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
public class OrderInfoControllerTest {

    @Resource
    private OrderInfoController orderInfoController;

    /**
     * 用户购买商品
     *
     * @return JsonResponse
     */
    @Test
    public void buyProductOrder() throws Exception {
        BuyProductOrderVO buyProductOrderVO = new BuyProductOrderVO();
        buyProductOrderVO.setUserNumber("13920002002");
        buyProductOrderVO.setMerchantNumber("13910001001");
        buyProductOrderVO.setSku("Nike-AJ1-38-WHITE");
        buyProductOrderVO.setProductQty(1);
        JsonResponse rsp = orderInfoController.buyProductOrder(buyProductOrderVO);
        log.info(JSON.toJSONString(rsp));
        Assert.assertTrue(200 == rsp.getStatus());
    }
}