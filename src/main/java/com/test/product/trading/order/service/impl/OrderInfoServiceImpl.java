package com.test.product.trading.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.product.trading.common.config.RedisKeysConfig;
import com.test.product.trading.common.enums.CodeEnum;
import com.test.product.trading.common.rsp.JsonResponse;
import com.test.product.trading.common.rsp.JsonResponseFactory;
import com.test.product.trading.common.tool.DateUtil;
import com.test.product.trading.common.tool.RedisUtil;
import com.test.product.trading.merchant.entity.MerchantAccount;
import com.test.product.trading.merchant.entity.MerchantAccountFlow;
import com.test.product.trading.merchant.entity.ProductStock;
import com.test.product.trading.merchant.service.MerchantAccountFlowService;
import com.test.product.trading.merchant.service.MerchantAccountService;
import com.test.product.trading.merchant.service.ProductStockService;
import com.test.product.trading.order.entity.AddOrderVO;
import com.test.product.trading.order.entity.BuyProductOrderVO;
import com.test.product.trading.order.entity.OrderInfo;
import com.test.product.trading.order.entity.OrderItem;
import com.test.product.trading.order.mapper.OrderInfoMapper;
import com.test.product.trading.order.service.OrderInfoService;
import com.test.product.trading.order.service.OrderItemService;
import com.test.product.trading.user.entity.UserAccount;
import com.test.product.trading.user.entity.UserAccountFlow;
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


/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author yh
 * @since 2025-06-16
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    private final UserAccountService userAccountService;

    private final UserAccountFlowService userAccountFlowService;

    private final MerchantAccountService merchantAccountService;

    private final MerchantAccountFlowService merchantAccountFlowService;

    private final ProductStockService productStockService;

    private final OrderItemService orderItemService;

    private final StringRedisTemplate redisTemplate;

    /**
     * 用户购买商品
     *
     * @param buyProductOrderVO 参数
     * @return JsonResponse
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse buyProductOrder(BuyProductOrderVO buyProductOrderVO) {
        log.info("OrderInfoServiceImpl buyProductOrder buyProductOrderVO:{} ", JSON.toJSONString(buyProductOrderVO));
        String userNumber = buyProductOrderVO.getUserNumber();
        if (StringUtils.isEmpty(userNumber)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "userNumber不存在");
        String merchantNumber = buyProductOrderVO.getMerchantNumber();
        if (StringUtils.isEmpty(merchantNumber)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "merchantNumber不存在");
        String sku = buyProductOrderVO.getSku();
        if (StringUtils.isEmpty(sku)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "sku不存在");
        Integer productQty = buyProductOrderVO.getProductQty();
        if (ObjectUtils.isEmpty(productQty)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "productQty不存在");

        boolean stockChange = false; //库存缓存变动标识
        String key = RedisKeysConfig.CACHE_MERCHANT_PRODUCT_STOCK_KEY;
        String mapKey = merchantNumber + ":" + sku;
        String lockKey = RedisKeysConfig.LOCK_ORDER_BUY_PRODUCT_SKU_KEY + ":" + userNumber + ":" + merchantNumber + ":" + sku;
        String lockValue = lockKey + ":" + String.valueOf(productQty);
        try {
            // 如果未获取到分布式锁，不执行后续代码
            boolean tryLock = RedisUtil.tryLock(redisTemplate, lockKey, lockValue, 60);
            if (!tryLock) {
                log.info("OrderInfoServiceImpl buyProductOrder，未获取到分布式锁，不执行后续代码 {} {}", lockKey, lockValue);
                return JsonResponseFactory.build(90001, "未获取到分布式锁", lockKey);
            }

            // 缓存中检查库存是否足够
            String mapValue = RedisUtil.getCacheMap(redisTemplate, key, mapKey);
            if (StringUtils.isEmpty(mapValue)) return cancelTrade(70000, "库存不存在", lockKey, "userNumber="+userNumber+" merchantNumber="+merchantNumber+" sku="+sku+" productQty="+String.valueOf(productQty)+" currentStock=null");
            int currentStock = Integer.valueOf(mapValue);
            String logtxt = "userNumber="+userNumber+" merchantNumber="+merchantNumber+" sku="+sku+" productQty="+String.valueOf(productQty)+" currentStock="+String.valueOf(currentStock);
            if (productQty.intValue() > currentStock) return cancelTrade(70000, "库存不足下单失败", lockKey, logtxt);

            // 检查并获取商户余额
            MerchantAccount merchantAccount = merchantAccountService.getMerchantAccount(merchantNumber, CodeEnum.DateEnum.LATEST.getCode());
            if (ObjectUtils.isEmpty(merchantAccount)) return cancelTrade(70000, "商户账户不存在", lockKey, logtxt);
            Long merchantId = merchantAccount.getMerchantId();
            Long merchantAccountId = merchantAccount.getId();
            BigDecimal merchantBalance = merchantAccount.getBalance();

            // 检查并获取商品
            ProductStock productStock = productStockService.getProductStock(merchantId, sku);
            if (ObjectUtils.isEmpty(productStock)) return cancelTrade(70000, "商品库存不存在", lockKey, logtxt);
            Long productId = productStock.getId();
            BigDecimal price = productStock.getPrice();
            int prdStock = productStock.getStock().intValue();

            // 检查并获取用户当前的账户余额
            BigDecimal totalAmount = BigDecimal.valueOf(price.doubleValue() * Double.valueOf(productQty.intValue()));
            UserAccount userAccount = userAccountService.getUserAccount(userNumber, CodeEnum.DateEnum.LATEST.getCode());
            if (ObjectUtils.isEmpty(userAccount)) return cancelTrade(70000, "用户账户不存在", lockKey, logtxt);
            Long userId = userAccount.getUserId();
            Long userAccountId = userAccount.getId();
            BigDecimal userBalance = userAccount.getBalance();
            if (userBalance.doubleValue() < totalAmount.doubleValue()) return cancelTrade(70000, "用户余额不足下单失败", lockKey, logtxt);

            // 缓存内减库存
            String thisStock = String.valueOf(Integer.valueOf(RedisUtil.getCacheMap(redisTemplate, key, mapKey)).intValue() - productQty.intValue());
            RedisUtil.setCacheMap(redisTemplate, key, mapKey, thisStock);
            stockChange = true;
            log.info("OrderInfoServiceImpl buyProductOrder 缓存内减库存 thisStock={}", RedisUtil.getCacheMap(redisTemplate, key, mapKey));

            // 生成订单信息
            String today = DateUtil.tranDateMonth2String(new Date());
            AddOrderVO addOrderVO = new AddOrderVO();
            addOrderVO.setProductId(productId);
            addOrderVO.setQuantity(productQty);
            addOrderVO.setPrice(price);
            addOrderVO.setUserId(userId);
            addOrderVO.setMerchantId(merchantId);
            addOrderVO.setTotalAmount(totalAmount);
            addOrderVO.setStatus(CodeEnum.StatusEnum.SUCCESS.getCode());
            addOrderVO.setCreateDt(today);
            JsonResponse orderResponse = addOrder(addOrderVO);
            log.info("OrderInfoServiceImpl buyProductOrder orderResponse={}", orderResponse);
            if (200 != orderResponse.getStatus()) throw new RuntimeException("生成订单处理异常");
            OrderInfo orderInfoEty = (OrderInfo) orderResponse.getData();
            Long orderId = orderInfoEty.getId();

            // 更新商品库存表
            ProductStock productStockEty = ProductStock.builder().id(productId).stock(prdStock - productQty.intValue()).build();
            boolean productStockFlag = productStockService.updateById(productStockEty);
            log.info("OrderInfoServiceImpl buyProductOrder productStockEty={} productStockFlag={}", productStockEty, productStockFlag);
            if (!productStockFlag) throw new RuntimeException("更新商品库存表异常");

            // 更新商家账户余额
            BigDecimal newBalance = merchantBalance.add(totalAmount);
            MerchantAccount merchantAccountEty = MerchantAccount.builder().id(merchantAccountId).balance(newBalance).build();
            boolean merchantAccountFlag = merchantAccountService.updateById(merchantAccountEty);
            log.info("OrderInfoServiceImpl buyProductOrder merchantAccountEty={} merchantAccountFlag={}", merchantAccountEty, merchantAccountFlag);
            if (!merchantAccountFlag) throw new RuntimeException("更新商家账户余额异常");

            // 添加商家账户流水
            MerchantAccountFlow merchantAccountFlowEty = MerchantAccountFlow.builder().accountId(merchantAccountId)
                    .amount(totalAmount).trdType(CodeEnum.TradeTypeEnum.BUY.getCode()).orderId(orderId).createDt(today).build();
            boolean merchantAccountFlowFlag = merchantAccountFlowService.save(merchantAccountFlowEty);
            log.info("OrderInfoServiceImpl buyProductOrder merchantAccountFlowEty={} merchantAccountFlowFlag={}", merchantAccountFlowEty, merchantAccountFlowFlag);
            if (!merchantAccountFlowFlag) throw new RuntimeException("添加商家账户流水异常");

            // 更新用户账户余额
            BigDecimal newUserBalance = BigDecimal.valueOf(userBalance.doubleValue() - totalAmount.doubleValue());
            UserAccount userAccountEty = UserAccount.builder().id(userAccountId).balance(newUserBalance).build();
            boolean userAccountFlag = userAccountService.updateById(userAccountEty);
            log.info("OrderInfoServiceImpl buyProductOrder userAccountEty={} userAccountFlag={}", userAccountEty, userAccountFlag);
            if (!userAccountFlag) throw new RuntimeException("更新用户账户余额异常");

            // 添加用户账户流水
            UserAccountFlow userAccountFlowEty = UserAccountFlow.builder().accountId(userAccountId)
                    .amount(BigDecimal.valueOf(-1 * totalAmount.doubleValue()))
                    .trdType(CodeEnum.TradeTypeEnum.BUY.getCode()).orderId(orderId).createDt(today).build();
            boolean userAccountFlowFlag = userAccountFlowService.save(userAccountFlowEty);
            log.info("OrderInfoServiceImpl buyProductOrder userAccountFlowEty={} userAccountFlowFlag={}", userAccountFlowEty, userAccountFlowFlag);
            if (!userAccountFlowFlag) throw new RuntimeException("添加用户账户流水异常");

            // 释放分布式锁
            boolean unLock = RedisUtil.unLock(redisTemplate, lockKey);
            return JsonResponseFactory.success(orderInfoEty);
        } catch (Exception e) {
            log.error("OrderInfoServiceImpl buyProductOrder异常！buyProductOrderVO:{} {}", JSON.toJSONString(buyProductOrderVO), e.getMessage());
            e.printStackTrace();
            // 缓存内恢复库存
            if (stockChange) RedisUtil.setCacheMap(redisTemplate, key, mapKey, String.valueOf(Integer.valueOf(RedisUtil.getCacheMap(redisTemplate, key, mapKey)).intValue() + productQty.intValue()) );
            RedisUtil.unLock(redisTemplate, lockKey);
            throw new RuntimeException("处理异常", e);
        }

    }

    /**
     * 生成订单信息
     *
     * @param addOrderVO 参数
     * @return JsonResponse
     */
    @Override
    public JsonResponse addOrder(AddOrderVO addOrderVO) throws Exception {
        log.info("OrderInfoServiceImpl addOrder addOrderVO:{} ", JSON.toJSONString(addOrderVO));
        Long userId = addOrderVO.getUserId();
        if (ObjectUtils.isEmpty(userId)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "userId不存在");
        Long merchantId = addOrderVO.getMerchantId();
        if (ObjectUtils.isEmpty(merchantId)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "merchantId不存在");
        BigDecimal totalAmount = addOrderVO.getTotalAmount();
        if (ObjectUtils.isEmpty(totalAmount)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "totalAmount不存在");
        Integer status = addOrderVO.getStatus();
        if (ObjectUtils.isEmpty(status)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "status不存在");
        String createDt = addOrderVO.getCreateDt();
        if (StringUtils.isEmpty(createDt)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "createDt不存在");
        Long productId = addOrderVO.getProductId();
        if (ObjectUtils.isEmpty(productId)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "productId不存在");
        Integer quantity = addOrderVO.getQuantity();
        if (ObjectUtils.isEmpty(quantity)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "quantity不存在");
        BigDecimal price = addOrderVO.getPrice();
        if (ObjectUtils.isEmpty(price)) return JsonResponseFactory.build(10000, "入参错误，无法执行", "price不存在");

        //生成订单信息
        OrderInfo orderInfo = OrderInfo.builder().userId(userId).merchantId(merchantId).totalAmount(totalAmount)
                .status(status).createDt(createDt).build();
        boolean r = save(orderInfo);
        if (!r) throw new RuntimeException("生成订单信息处理异常");
        //生成订单明细
        OrderItem orderItem = OrderItem.builder().orderId(orderInfo.getId()).productId(productId)
                .quantity(quantity).price(price).build();
        boolean s = orderItemService.save(orderItem);
        if (!s) throw new RuntimeException("生成订单明细处理异常");
        return JsonResponseFactory.success(orderInfo);
    }

    private JsonResponse cancelTrade(int status, String message, String lockKey, String logtxt) {
        log.info("OrderInfoServiceImpl buyProductOrder，{} {}", message, logtxt);
        RedisUtil.unLock(redisTemplate, lockKey);
        return JsonResponseFactory.build(status, message, logtxt);
    }

}
