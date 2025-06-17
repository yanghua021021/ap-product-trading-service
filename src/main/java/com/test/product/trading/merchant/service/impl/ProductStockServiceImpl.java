package com.test.product.trading.merchant.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.product.trading.common.config.RedisKeysConfig;
import com.test.product.trading.common.constant.ConstDefine;
import com.test.product.trading.common.enums.CodeEnum;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.common.rsp.JsonResponseFactory;
import com.test.product.trading.common.tool.RedisUtil;
import com.test.product.trading.merchant.entity.MerchantAccount;
import com.test.product.trading.merchant.entity.MerchantInfo;
import com.test.product.trading.merchant.entity.ProductStock;
import com.test.product.trading.merchant.entity.ProductStockVO;
import com.test.product.trading.merchant.mapper.MerchantInfoMapper;
import com.test.product.trading.merchant.mapper.ProductStockMapper;
import com.test.product.trading.merchant.service.ProductStockService;
import com.test.product.trading.user.entity.UserAccount;
import com.test.product.trading.user.entity.UserAccountFlow;
import com.test.product.trading.user.entity.UserInfo;
import com.test.product.trading.user.mapper.UserInfoMapper;
import com.test.product.trading.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 商品库存表 服务实现类
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductStockServiceImpl extends ServiceImpl<ProductStockMapper, ProductStock> implements ProductStockService {

    private final StringRedisTemplate redisTemplate;

    private final MerchantInfoMapper merchantInfoMapper;

    private final ProductStockMapper productStockMapper;

    /**
     * 添加或更新商品库存表
     *
     * @param productStock 参数
     * @return JsonResponse
     */
    @Override
    public JsonResponse addProductStock(ProductStock productStock) {
        log.info("ProductStockServiceImpl addProductStock productStock:{} ", JSON.toJSONString(productStock));
        Long merchantId = productStock.getMerchantId();
        if (ObjectUtils.isEmpty(merchantId)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "merchantId不存在");
        String sku = productStock.getSku();
        if (StringUtils.isEmpty(sku)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "sku不存在");
        String name = productStock.getName();
        if (StringUtils.isEmpty(name)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "name不存在");
        BigDecimal price = productStock.getPrice();
        if (ObjectUtils.isEmpty(price)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "price不存在");
        Integer stock = productStock.getStock();
        if (ObjectUtils.isEmpty(stock)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "stock不存在");
        try {
            boolean r = saveOrUpdate(productStock);
        } catch (Exception e) {
            log.error("ProductStockServiceImpl addProductStock异常！{}", e.getMessage());
            e.printStackTrace();
            return JsonResponseFactory.error(99999);
        }

        return JsonResponseFactory.success(productStock);
    }

    /**
     * 添加商品数量
     *
     * @param productStockVO 参数
     * @return JsonResponse
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse addMerchantProductQty(ProductStockVO productStockVO) {
        log.info("ProductStockServiceImpl addMerchantProductQty productStockVO:{} ", JSON.toJSONString(productStockVO));
        String merchantNumber = productStockVO.getMerchantNumber();
        if (StringUtils.isEmpty(merchantNumber)) {
            return JsonResponseFactory.build(10000, "入参错误，无法执行", "merchantNumber不存在");
        }
        String sku = productStockVO.getSku();
        if (StringUtils.isEmpty(sku)) {
            return JsonResponseFactory.build(10000, "入参错误，无法执行", "sku不存在");
        }
        Integer productQty = productStockVO.getProductQty();
        if (ObjectUtils.isEmpty(productQty)) {
            return JsonResponseFactory.build(10000, "入参错误，无法执行", "productQty不存在");
        }

        try {
            // 检查商户
            MerchantInfo merchantInfo = merchantInfoMapper.selectOne(
                    new QueryWrapper<MerchantInfo>().lambda()
                            .eq(MerchantInfo::getMerchantNumber, merchantNumber)
                            .last(ConstDefine.LIMIT_ONE));
            if (ObjectUtils.isEmpty(merchantInfo)) {
                log.info("ProductStockServiceImpl addMerchantProductQty，商户不存在 merchantNumber={}", merchantNumber);
                return JsonResponseFactory.build(10000, "入参错误，无法执行", "商户不存在"+merchantNumber);
            }
            Long merchantId = merchantInfo.getId();
            // 检查商品库存
            ProductStock productStock = productStockMapper.selectOne(
                    new QueryWrapper<ProductStock>().lambda()
                            .eq(ProductStock::getMerchantId, merchantId)
                            .eq(ProductStock::getSku, sku)
                            .last(ConstDefine.LIMIT_ONE));
            ProductStock newEntity = ProductStock.builder().merchantId(merchantId).sku(sku)
                    .name(productStockVO.getName()).price(productStockVO.getPrice()).build();
            Integer latestStock = productQty;
            newEntity.setStock(latestStock);
            if (ObjectUtils.isNotEmpty(productStock)) {
                Integer currentStock = productStock.getStock();
                latestStock += currentStock.intValue();
                newEntity.setStock(latestStock);
                newEntity.setId(productStock.getId());
            }
            boolean r = saveOrUpdate(newEntity);
            // 商家的商品库存写入redis缓存
            if (r) {
                String key = RedisKeysConfig.CACHE_MERCHANT_PRODUCT_STOCK_KEY;
                String mapKey = String.valueOf(merchantNumber) + ":" + sku;
                log.info("ProductStockServiceImpl addMerchantProductQty，currentValue={}", RedisUtil.getCacheMap(redisTemplate, key, mapKey));
                RedisUtil.setCacheMap(redisTemplate, key, mapKey, String.valueOf(latestStock));
                String cacheMapValue = RedisUtil.getCacheMap(redisTemplate, key, mapKey);
                log.info("ProductStockServiceImpl addMerchantProductQty，latestValue={}", cacheMapValue);
            }
            return JsonResponseFactory.success(newEntity);
        } catch (Exception e) {
            log.error("ProductStockServiceImpl addMerchantProductQty异常！{}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("处理异常", e);
        }

    }

    /**
     * 获取商品
     *
     * @param merchantId 参数
     * @param sku 参数
     * @return ProductStock
     */
    @Override
    public ProductStock getProductStock(Long merchantId, String sku) {
        try {
            ProductStock productStock = productStockMapper.selectOne(
                    new QueryWrapper<ProductStock>().lambda()
                            .eq(ProductStock::getMerchantId, merchantId)
                            .eq(ProductStock::getSku, sku)
                            .last(ConstDefine.LIMIT_ONE));
            return productStock;
        } catch (Exception e) {
            log.error("ProductStockServiceImpl getProductStock异常！{} {} {}", merchantId, sku, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
