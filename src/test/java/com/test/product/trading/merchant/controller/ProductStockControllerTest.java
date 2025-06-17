package com.test.product.trading.merchant.controller;

import com.alibaba.fastjson.JSON;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.merchant.entity.ProductStockVO;
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
public class ProductStockControllerTest {

    @Resource
    private ProductStockController productStockController;

    /**
     * 添加商品数量
     *
     * @return JsonResponse
     */
    @Test
    public void addMerchantProductQty() throws Exception {
        ProductStockVO productStockVO = new ProductStockVO();
        productStockVO.setMerchantNumber("13910001001");
        productStockVO.setSku("Nike-AJ1-38-WHITE");
        productStockVO.setProductQty(10);
        productStockVO.setName("Nike Air Jordan 第1代篮球鞋 38码 白色");
        productStockVO.setPrice(BigDecimal.valueOf(1299.99));
        JsonResponse rsp = productStockController.addMerchantProductQty(productStockVO);
        log.info(JSON.toJSONString(rsp));
        Assert.assertTrue(200 == rsp.getStatus());
    }
}
