package com.test.product.trading.merchant.controller;


import com.alibaba.fastjson.JSON;
import com.test.product.trading.common.config.UrlConfig;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.common.rsp.JsonResponseFactory;
import com.test.product.trading.merchant.entity.MerchantInfo;
import com.test.product.trading.merchant.service.MerchantAccountService;
import com.test.product.trading.merchant.service.MerchantInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 商家信息表 前端控制器
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MerchantInfoController {

    private final MerchantInfoService merchantInfoService;

    /**
     * 添加商户
     *
     * @param merchantInfo 参数
     * @return JsonResponse
     */
    @PostMapping(value = UrlConfig.URL_MERCHANT_INFO_ADD_V1, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonResponse addMerchant(@RequestBody MerchantInfo merchantInfo) {
        log.info("MerchantInfoController addMerchant merchantInfo:{} ", JSON.toJSONString(merchantInfo));
        return merchantInfoService.addMerchant(merchantInfo);
    }

}

